package ca.georgiancollege.ice6

@Suppress("PropertyName")
class Person(private var name: String, private var age: Float)
{
   // Properties with custom getters and setters
   var Name
      get() = name
      set(value) {
         if (value.isNotBlank()) name = value
      }

   var Age
      get() = age
      set(value) {
         if (value >= 0) age = value
      }

   override fun toString(): String
   {
      return "Person(name='$name', age=$age)"
   }
}
