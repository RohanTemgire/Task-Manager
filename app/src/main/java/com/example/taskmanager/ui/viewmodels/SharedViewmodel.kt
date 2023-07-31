package com.example.taskmanager.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.models.Priority
import com.example.taskmanager.data.models.TaskEntity
import com.example.taskmanager.data.repository.DataStoreRepository
import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.utils.Actions
import com.example.taskmanager.utils.Constants.TITLE_CHAR_LIMIT
import com.example.taskmanager.utils.RequestState
import com.example.taskmanager.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewmodel @Inject constructor(
    private val repository: TaskRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val action: MutableState<Actions> = mutableStateOf(Actions.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<TaskEntity>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<TaskEntity>>> = _searchedTasks

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    // statein is used to convert flow into stateflow
    val lowPriorityTasks: StateFlow<List<TaskEntity>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<TaskEntity>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState.map { Priority.valueOf(it) }.collect {
                    _sortState.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }


    private val _allTasks =
        MutableStateFlow<RequestState<List<TaskEntity>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<TaskEntity>>> = _allTasks

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<TaskEntity?> = MutableStateFlow(null)
    val selectedTask: StateFlow<TaskEntity?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = TaskEntity(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.addTask(taskEntity = task)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = TaskEntity(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.updateTask(taskEntity = task)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = TaskEntity(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repository.deleteTask(taskEntity = task)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Actions) {
        when (action) {
            Actions.ADD -> {
                addTask()
            }

            Actions.UPDATE -> {
                updateTask()
            }

            Actions.DELETE -> {
                deleteTask()
            }

            Actions.DELETE_ALL -> {
                deleteAllTasks()
            }

            Actions.UNDO -> {
                addTask()
            }

            else -> {

            }
        }
        this.action.value = Actions.NO_ACTION
    }

    fun updateTaskFields(selectedTask: TaskEntity?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }


    fun validateFields(): Boolean {
        return title.value.isNotEmpty()
    }

}