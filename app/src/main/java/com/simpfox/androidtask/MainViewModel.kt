package com.simpfox.androidtask

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpfox.androidtask.database.entity.SortType
import com.simpfox.androidtask.repository.TaskRepo
import com.simpfox.androidtask.ui.AppMenuItem
import com.simpfox.androidtask.ui.pagertab.state.TabUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskGroupUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskPageUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskUiState
import com.simpfox.androidtask.ui.pagertab.state.millisToStringDate
import com.simpfox.androidtask.ui.pagertab.state.toTabUiState
import com.simpfox.androidtask.ui.pagertab.state.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_FAVORITE_LIST = -1000L
@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel(), TaskDelegate {

    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> = MutableStateFlow(emptyList()) // Tai sao lai dung StateFlow ma khong phai LiveData
    val listTabGroup = _listTabGroup.map {
        listOf(TaskGroupUiState(
            tab = TabUiState(ID_FAVORITE_LIST, "Fav", SortType.CREATED_DATE),
            page = TaskPageUiState(mutableListOf<TaskUiState>().apply {
                it.forEach { tab ->
                    addAll(tab.page.activeTaskList.filter { task -> task.isFavorite })
                }
            }.sortedByDescending { task -> task.updatedAt }, emptyList()))
        ) + it.map { tabItem ->
            val sortType = tabItem.tab.sortType
            val activeTaskList = tabItem.page.activeTaskList
            tabItem.copy(
                tab = tabItem.tab,
                page = tabItem.page.copy(
                    activeTaskList = if (sortType == SortType.FAVORITE) {
                        activeTaskList.sortedByDescending { task -> task.isFavorite }
                    } else {
                        activeTaskList.sortedByDescending { task -> task.createdAt }
                    },
                    completedTaskList = tabItem.page.completedTaskList.sortedByDescending { task -> task.updatedAt }
                )
            )
        } +
            TaskGroupUiState(
            tab = TabUiState(ID_ADD_NEW_LIST, "Add New List", SortType.CREATED_DATE),
            page = TaskPageUiState(emptyList(), emptyList())
        )
    } // Tai sao lai phai as StateFlow -> de chi doc

    private var _currentSelectedCollectionId : Long = -1

    private val _eventFlow : MutableSharedFlow<MainEvent> = MutableSharedFlow() // Su dung SharedFlow de phat su kien mot lan
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val listTaskCollections = taskRepo.getTaskCollection()
            val listTabGroupUiState = listTaskCollections.ifEmpty {
                taskRepo.addTaskCollection("My Tasks")?.let { collection ->
                    val id = collection.id
                    taskRepo.addTask("Task 1", id)
                    taskRepo.addTask("Task 2", id)
                    taskRepo.addTask("Task 3", id)
                }
                taskRepo.getTaskCollection()
            }.map { taskCollection ->
                val collectionId = taskCollection.id
                val listTaskUiState = taskRepo.getTasksByCollectionId(collectionId).map { taskEntity ->
                    taskEntity.toTaskUiState()
                }
                val tabUiState = taskCollection.toTabUiState()
                TaskGroupUiState(tabUiState, TaskPageUiState(
                    activeTaskList = listTaskUiState.filter { !it.isCompleted },
                    completedTaskList = listTaskUiState.filter { it.isCompleted }
                ))
            }
            _listTabGroup.value = listTabGroupUiState
        }
//        _listTabGroup.value = listOf(
//            TaskGroupUiState(
//                tab = TabUiState(
//                    id = 1,
//                    title = "Tab 1"
//                ),
//                page = TaskPageUiState(
//                    listOf(
//                        TaskUiState(
//                            id = 1,
//                            content = "Task 1",
//                            collectionId = 1,
//                        ),
//                        TaskUiState(
//                            id = 2,
//                            content = "Task 2",
//                            collectionId = 1,
//                        ),
//                        TaskUiState(
//                            id = 3,
//                            content = "Task 3",
//                            collectionId = 1,
//                            isFavorite = true
//                        ),
//                    ), listOf()
//                ),
//            ),
//            TaskGroupUiState(
//                tab = TabUiState(
//                    id = 2,
//                    title = "Tab 2"
//                ),
//                page = TaskPageUiState(
//                    listOf(), listOf()
//                ),
//            ),
//        )
    }

    override fun invertTaskFavorite(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isFavorite = !taskUiState.isFavorite)
            if (!taskRepo.updateTaskFavorite(newTaskUiState.id, newTaskUiState.isFavorite)) {
                return@launch
            }
            _listTabGroup.value.let { listTabGroup ->
                val newTabGroup = listTabGroup.map { tabGroup ->
                    val currentTime = Calendar.getInstance().timeInMillis
                    val newPage = tabGroup.page.copy(
                        activeTaskList = tabGroup.page.activeTaskList.map { task ->
                            if (task.id == newTaskUiState.id) newTaskUiState.copy(
                                updatedAt = currentTime,
                                stringUpdatedAt = currentTime.millisToStringDate()
                            ) else task
                        },
                        completedTaskList = tabGroup.page.completedTaskList.map { task ->
                            if (task.id == newTaskUiState.id) newTaskUiState.copy(
                                updatedAt = currentTime,
                                stringUpdatedAt = currentTime.millisToStringDate()
                            ) else task
                        }
                    )
                    tabGroup.copy(page = newPage)
                }
                _listTabGroup.value = newTabGroup
            }
        }
    }

    override fun invertTaskCompleted(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isCompleted = !taskUiState.isCompleted)
            if (!taskRepo.updateTaskCompleted(newTaskUiState.id, newTaskUiState.isCompleted)) {
                Log.e("MainViewModel", "Failed to update task completed")
                return@launch
            }
            _listTabGroup.value.let { listTabGroup ->
                val newTabGroup = listTabGroup.map { tabGroup ->
                    val sumList = tabGroup.page.activeTaskList + tabGroup.page.completedTaskList
                    val updatedList = sumList.map { task ->
                        if (task.id == newTaskUiState.id) {
                            val newUpdatedAt = Calendar.getInstance().timeInMillis
                            newTaskUiState.copy(
                                updatedAt = newUpdatedAt,
                                stringUpdatedAt = newUpdatedAt.millisToStringDate()
                            )
                        } else {
                            task
                        }
                    }
                    val newPage = tabGroup.page.copy(
                        activeTaskList = updatedList.filter { !it.isCompleted },
                        completedTaskList = updatedList.filter { it.isCompleted }
                    )
                    tabGroup.copy(page = newPage)
                }
                _listTabGroup.value = newTabGroup
            }
        }
    }

    override fun addNewTask(collectionId: Long, content: String) {
        viewModelScope.launch {
            taskRepo.addTask(content, collectionId)?.let { taskEntity ->
                val newTaskUiState = taskEntity.toTaskUiState()
                _listTabGroup.value.let { listTabGroup ->
                    val newTabGroup = listTabGroup.map { tabGroup ->
                        if(tabGroup.tab.id == collectionId) {
                            val newPage = tabGroup.page.copy(
                                activeTaskList = (tabGroup.page.activeTaskList + newTaskUiState).sortedBy { it.updatedAt }
                            )
                            tabGroup.copy(page = newPage)
                        } else {
                            tabGroup
                        }
                    }
                    _listTabGroup.value = newTabGroup
                }
            }
        }
    }

    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            _listTabGroup.value.firstOrNull() { it.tab.id == _currentSelectedCollectionId }?.let { currentTab ->
                val collectionId = currentTab.tab.id
                if (collectionId > 0) {
                    addNewTask(collectionId, content)
                }
            }
        }
    }



    override fun addNewCollection(title: String) {
        viewModelScope.launch {
            taskRepo.addTaskCollection(title)?.let { taskCollection ->
                val tabUiState = taskCollection.toTabUiState()
                val newTabGroup = TaskGroupUiState(tabUiState, TaskPageUiState(emptyList(), emptyList()))
                _listTabGroup.value += newTabGroup
            }
        }
    }

    override fun requestAddNewCollection() {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestAddNewCollection)
        }
    }

    override fun updateCurrentCollectionId(collectionId: Long) { // Chua hieu
        _currentSelectedCollectionId = collectionId
    }

    override fun currentCollectionId(): Long {
        return _currentSelectedCollectionId
    }

    override fun requestUpdateCollection(collectionId: Long) {
        Log.d("MainViewModel", "Request update collection $collectionId")
        val actionList = listOf(
            AppMenuItem("Delete Collection") {
                deleteCollectionById(collectionId)
            },
            AppMenuItem("Rename Collection") {
                Log.d("MainViewModel", "Request rename collection $collectionId")
            }
        )
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestShowBottomSheetOptions(actionList))
        }
    }

    private fun deleteCollectionById(collectionId: Long) {
        viewModelScope.launch {
            if(taskRepo.deleteTaskCollectionById(collectionId)) {
                _listTabGroup.value.let { listTabs ->
                    val newTabGroup = listTabs.filter { tabItem -> tabItem.tab.id != collectionId }
                    _listTabGroup.value = newTabGroup
                }
            }
        }
    }

    override fun requestSortTask(collectionId: Long) {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestShowBottomSheetOptions(listOf(
                AppMenuItem("Sort By Favourite") {
                    sortCollectionBy(collectionId, SortType.FAVORITE)
                },
                AppMenuItem("Sort By Created Date") {
                    sortCollectionBy(collectionId, SortType.CREATED_DATE)
                }
            )))
        }
    }

    private fun sortCollectionBy(collectionId: Long, sortType: SortType) {
        viewModelScope.launch {
            if (taskRepo.updateCollectionSortType(collectionId, sortType.value)) {
                _listTabGroup.value.let { listTabs ->
                    val newTabGroup = listTabs.map { tabGroup ->
                        if(tabGroup.tab.id == collectionId) {
                            tabGroup.copy(tab = tabGroup.tab.copy(
                                sortType = sortType
                            ))
                        } else {
                            tabGroup
                        }
                    }
                    _listTabGroup.value = newTabGroup
                }
            }
        }
    }
}



interface TaskDelegate {
    fun invertTaskFavorite(taskUiState: TaskUiState) = Unit
    fun invertTaskCompleted(taskUiState: TaskUiState) = Unit
    fun addNewTask(collectionId: Long,content: String) = Unit
    fun addNewTaskToCurrentCollection(content: String) = Unit
    fun updateCurrentCollectionId(collectionId: Long) = Unit
    fun currentCollectionId(): Long = -1L
    fun addNewCollection(title: String) = Unit
    fun requestAddNewCollection() = Unit
    fun requestUpdateCollection(collectionId: Long) = Unit
    fun requestSortTask(collectionId: Long) = Unit
}



sealed class MainEvent {
    data object RequestAddNewCollection : MainEvent()
    data class RequestShowBottomSheetOptions(val list: List<AppMenuItem>) : MainEvent()
}