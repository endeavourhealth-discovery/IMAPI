package org.endeavourhealth.imapi.model.codegen

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class TimeUnits {
    private val year: Int
    private val month: Int
    private val dayOfMonth: Int
    private val hour: Int
    private val minute: Int
    private val second: Int
    private val nanoSecond: Int

    constructor(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanoSecond: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
        this.hour = hour
        this.minute = minute
        this.second = second
        this.nanoSecond = nanoSecond
    }

    constructor(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
        this.hour = hour
        this.minute = minute
        this.second = second
        this.nanoSecond = 0
    }

    constructor(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
        this.hour = hour
        this.minute = minute
        this.second = 0
        this.nanoSecond = 0
    }

    constructor(year: Int, month: Int, dayOfMonth: Int, hour: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
        this.hour = hour
        this.minute = 0
        this.second = 0
        this.nanoSecond = 0
    }

    constructor(year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
        this.hour = 0
        this.minute = 0
        this.second = 0
        this.nanoSecond = 0
    }

    constructor(year: Int, month: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = 1
        this.hour = 0
        this.minute = 0
        this.second = 0
        this.nanoSecond = 0
    }

    constructor(year: Int) {
        this.year = year
        this.month = 1
        this.dayOfMonth = 1
        this.hour = 0
        this.minute = 0
        this.second = 0
        this.nanoSecond = 0
    }
}
