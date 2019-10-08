package edu.info448.kotlin

import javax.security.auth.callback.Callback
import javax.security.sasl.AuthorizeCallback
import kotlin.reflect.KProperty


/* See assignment instructions on Canvas */

open class Employee(val name: String, open var hourlyWage: Double) {
    var hoursWorked: Int = 0
        set(value) {
            if (value < 0)
                field = 0
            else if (value > 168)
                field = 168
            else field = value
        }
    init {
        println("Hello $name")
    }
    fun calculatePay(): Double {
        return hourlyWage * hoursWorked
    }
    fun showInvoice() {
        println("Pay @name \$${calculatePay()} for $hoursWorked hours of work")
    }
    open fun work( call:() -> Int) {
        hoursWorked += call()
    }
}

class UnionEmployee(val nam: String): Employee(name, 15.0) {
    val MINIMUM_WAGE = 15.0
    override var hourlyWage: Double by UnionContract()
    var onStrike: Boolean = false
    fun picket() {
        println("On Strike!")
    }
    override fun work(call:() -> Int) {
        if (onStrike)
            picket()
        else
            super.work(call)
    }
}

class UnionContract() {
    companion object {
        var negotiatedHourlyWage: Double = 0.0
        fun negotiate(wag: Double) {
            negotiatedHourlyWage = wag
        }
    }
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        return negotiatedHourlyWage
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
    }
}

class Job(val description: String, val hoursRequired: Int): () -> Int {
    override operator fun invoke(): Int {
        println("Doing job $description")
        return hoursRequired
    }
}

//A main() method in Kotlin
fun main(args: Array<String>) {
    var job = Job("tutor", 15)
    val employee: Array<UnionEmployee> = arrayOf(UnionEmployee("zach"), UnionEmployee("ann"))
    for (emp in employee) {
        emp.onStrike = true
        emp.work {
            println("working hard")
            10
        }
        emp.showInvoice();
    }
    UnionContract.negotiate(50.0)
    for (emp in employee) {
        emp.onStrike = false
        emp.work (job)
        emp.showInvoice();
    }
}