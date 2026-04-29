package com.example.cicdflow

import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun `add task should update list`() {
        viewModel.addTask("Test")

        val tasks = viewModel.tasks.getOrAwaitValue()

        assert(tasks.isNotEmpty())
    }
}
