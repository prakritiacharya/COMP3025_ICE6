package ca.georgiancollege.ice6

import android.annotation.SuppressLint
import android.widget.Button
import ca.georgiancollege.ice6.databinding.ActivityMainBinding

class Calculator (private var binding: ActivityMainBinding)
{
   // Lists of buttons for easy access
   private lateinit var numberButtons: List<Button>
   private lateinit var operatorButtons: List<Button>
   private lateinit var modifierButtons: List<Button>

   // private variables to hold the current state of the calculator
   private var currentOperand: String = ""
   private var currentOperator: String = ""
   private var dirtyFlag = false

   init {
      initializeButtonLists()
      configureNumberInput()
      configureModifierButtons()
      configureOperatorButtons()
   }

   /**
    * Initializes the lists of buttons for numbers, operators, and modifiers.
    * This allows for easy access to all buttons in the calculator layout.
    *
    * @param binding [ActivityMainBinding] The binding object for the main activity layout.
    */
   private fun initializeButtonLists()
   {
      // Initialize number buttons
      numberButtons = listOf(
         binding.zeroButton, binding.oneButton, binding.twoButton,
         binding.threeButton, binding.fourButton, binding.fiveButton,
         binding.sixButton, binding.sevenButton, binding.eightButton,
         binding.nineButton, binding.decimalButton
      )

      // Initialize operator buttons
      operatorButtons = listOf(
         binding.plusButton, binding.minusButton,
         binding.multiplyButton, binding.divideButton, binding.percentButton,
         binding.equalsButton
      )

      modifierButtons = listOf(
         binding.plusMinusButton,
         binding.clearButton, binding.deleteButton
      )
   }

   /**
    * Configures the number input buttons to handle clicks and update the result EditText.
    * It prevents multiple decimal points in the current number and handles leading zeros.
    */
   private fun configureNumberInput()
   {
      numberButtons.forEach { button ->
         button.setOnClickListener {
            if(!dirtyFlag)
            {
               val input = button.text.toString()
               val currentResultText = binding.resultEditText.text.toString()

               // Prevent multiple decimal points in the current number
               if (input == "." && currentResultText.contains("."))
               {
                  return@setOnClickListener // Do nothing if a decimal already exists
               }

               // If the current result is "0" and input is not ".", replace it
               if (currentResultText == "0" && input != ".")
               {
                  binding.resultEditText.setText(input)
               }
               else
               {
                  binding.resultEditText.append(input)
               }
            }
         }
      }
   }

   /**
    * Configures the modifier buttons (clear, delete, plus/minus) to handle clicks.
    * - Clear button resets the result to "0".
    * - Delete button removes the last character from the result.
    * - Plus/Minus button toggles the sign of the current number.
    */
   private fun configureModifierButtons()
   {
      modifierButtons.forEach { button ->
         button.setOnClickListener {
            when (button)
            {
               binding.clearButton -> {
                  resetCalculator()
               }
               binding.deleteButton ->
               {
                  val currentText = binding.resultEditText.text.toString()
                  if (currentText.isNotEmpty())
                  {
                     val newText = currentText.dropLast(1)

                     // If only "-" remains after deleting, reset to "0"
                     if (newText == "-" || newText.isEmpty())
                     {
                        if(dirtyFlag) resetCalculator() else binding.resultEditText.setText("0")
                     }
                     else
                     {
                        binding.resultEditText.setText(newText)
                     }
                  }
               }
               binding.plusMinusButton ->
               {
                  val currentText = binding.resultEditText.text.toString()
                  if (currentText.isNotEmpty())
                  {
                     // don't allow changing sign if the current text is "0" or empty
                     if (currentText == "0" || currentText.isEmpty())
                     {
                        return@setOnClickListener // Do nothing
                     }
                     // if the current text is already negative, remove the negative sign
                     if (currentText.startsWith("-"))
                     {
                        binding.resultEditText.setText(currentText.removePrefix("-"))
                     }
                     else
                     {
                        val prefixedCurrentText = "-$currentText"
                        binding.resultEditText.setText(prefixedCurrentText)
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Resets the calculator to its initial state.
    * This method clears the result EditText, resets the current operand and operator,
    * and sets the dirty flag to false.
    */
   private fun resetCalculator()
   {
      binding.resultEditText.setText("0")
      currentOperand = ""
      currentOperator = ""
      dirtyFlag = false
   }

   @SuppressLint("SetTextI18n")
   private fun configureOperatorButtons()
   {
      operatorButtons.forEach { button ->
         button.setOnClickListener {

            when (button)
            {
               binding.equalsButton -> {
                  if(currentOperator != "" && currentOperand != "0" && !dirtyFlag )
                  {
                     computeResult(currentOperand, binding.resultEditText.text.toString(), currentOperator)

                     dirtyFlag = true
                  }
               }
               else -> {
                  if(currentOperator == "")
                  {
                     currentOperator = button.tag.toString()
                     currentOperand = binding.resultEditText.text.toString()
                     binding.resultEditText.setText("0")
                  }
               }
            }
         }
      }
   }

   /**
    * Updates the result EditText with the current value.
    * This method is called to refresh the displayed value after any operation.
    *
    * @param lhs [String] ]The left-hand side operand as a String.
    * @param rhs [String] The right-hand side operand as a String.
    * @param operator [String] The operator to apply (e.g., "add", "subtract", "multiply", "divide", "percent").
    */
   fun computeResult(lhs:String, rhs:String, operator: String)
   {
      val leftOperand = lhs.toFloat()
      val rightOperand = rhs.toFloat()
      var result = ""

      when (operator)
      {
         "add" -> result = (leftOperand + rightOperand).toString()
         "subtract" -> result = (leftOperand - rightOperand).toString()
         "multiply" -> result = (leftOperand * rightOperand).toString()
         "divide" -> result =
            if (rightOperand != 0.0f)
            {
               (leftOperand / rightOperand).toString()
            }
            else{
               "Error: Division by zero"
            }
         "percent" -> result = ((leftOperand * rightOperand) / 100).toString()
      }

      binding.resultEditText.setText(result)
   }
}