package com.example.myapplication.ui.schedule

import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface ScheduleContract {

    interface View: BaseView<Presenter>{

    }

    interface Presenter: BasePresenter{

    }
}