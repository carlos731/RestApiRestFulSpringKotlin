package br.com.api.math

import org.springframework.web.bind.annotation.PathVariable

class SimpleMath {

    fun sum(numberOne: Double, numberTwo: Double) = numberOne + numberTwo

    fun subtration(numberOne: Double, numberTwo: Double) = numberOne - numberTwo

    fun multiplication(numberOne: Double, numberTwo: Double) = numberOne * numberTwo

    fun division(numberOne: Double, numberTwo: Double) = numberOne / numberTwo

    fun mean(numberOne: Double, numberTwo: Double) = (numberOne + numberTwo) / 2

    fun squareRoot(number: Double) = Math.sqrt(number)

}