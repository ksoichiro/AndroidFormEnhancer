AndroidFormEnhancer
===

AndroidFormEnhancer is a library for implementing input forms easily in the Android application.
You can use annotations to describe the definitions of the input form briefly,
reduce codes that handle data of the screens in the Activities and Fragments.

![Screenshot](https://raw.github.com/ksoichiro/AndroidFormEnhancer/master/samples/images/screenshot.png "Screenshots")


Install
===

The 'library' folder is the main library.
Please import it into the Eclipse or other IDEs.


Usage
===

1.  Create POJO class of the input form, define public fields and add annotations to them.

    ```java
    public class DefaultForm {
        @Widget(id = R.id.textfield_name)
        @Required
        public String name;

        @Widget(id = R.id.textfield_age, validateAfter = R.id.textfield_name)
        @IntType
        public String age;
    }
    ```

1.  If you use types other than String, create an entity class which fields has the same names as
    the form class.

    ```java
    public class DefaultEntity {
        public String name;
        public int age;
    }
    ```

1.  Write the codes like following to the Activity or Fragment to extract data from the screen,
    validate them and convert types.

    ```java
    ValidationResult result = new FormHelper(DefaultForm.class, this).validate();
    if (result.hasError()) {
        // Show error messages
        Toast.makeText(this, result.getAllSerializedErrors(), Toast.LENGTH_SHORT).show();
    } else {
        // This entity object has clean and converted data
        DefaultEntity entity = helper.create(DefaultEntity.class);
    }
    ```

1.  If you want to validate as soon as the focus changed, just write these codes:

    ```java
    new FormHelper(DefaultForm.class, this).setOnFocusOutValidation();
    ```

    Note that this method affects only for the text fields.


Get input values
===

To get input values from the layouts, create a Form class at first.
Form class is just a POJO class, which has public fields.
All the fields must be public and their types must be `String` or `java.util.List<String>`.

```java
public class DefaultForm {
    public String name;
}
```

Each fields must be related to the widgets like `android.widget.EditText`.
To create relationship with widgets, add special annotations to the fields.

## EditText
If you use `<EditText>`, then just use `@Widget` for the related field of the Form class.
For example, if the part of your `res/layout/some_layout.xml` is like this:

```xml
<EditText
    android:id="@+id/textfield_name"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

then you should define your Form class like this:

```java
public class DefaultForm {
    @Widget(id = R.id.textfield_name)
    public String name;
}
```

## RadioGroup and RadioButton
If you use `<RadioGroup>` and `<RadioButton>`, and you want to validate
whether the one of the radio buttons is checked or not,
then use `@Widget` and `@WidgetValue` for the related field of the Form class.
For example, if the part of your `res/layout/some_layout.xml` is like this:

```xml
<RadioGroup
    android:id="@+id/rg_gender"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <RadioButton
        android:id="@+id/rb_male"
        android:text="Male"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <RadioButton
        android:id="@+id/rb_female"
        android:text="Female"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</RadioGroup>
```

then you should define your Form class like this:

```java
public class DefaultForm {
    @Widget(id = R.id.rg_gender,
        values = {
            @WidgetValue(id = R.id.rb_male, value = "M"),
            @WidgetValue(id = R.id.rb_female, value = "F"),
        })
    public String gender;
}
```

If you choose radio button "Male", then the value of the `DefaultForm#gender` will be "M".

## CheckBox
If you use `<CheckBox>` and you want to validate that at least one check box is checked,
then use `@Widget` and `@WidgetValue` for the related field of the Form class.
For example, if the part of your `res/layout/some_layout.xml` is like this:

```xml
<LinearLayout
    android:id="@+id/cbg_sns"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">
    <CheckBox
        android:id="@+id/cb_facebook"
        android:text="Facebook"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <CheckBox
        android:id="@+id/cb_googleplus"
        android:text="Google+"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <CheckBox
        android:id="@+id/cb_twitter"
        android:text="Twitter"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</LinearLayout>
```

then you should define your Form class like this:

```java
public class DefaultForm {
    @Widget(id = R.id.cbg_sns,
        atLeast = 1,
        values = {
            @WidgetValue(id = R.id.cb_facebook, value = "FB"),
            @WidgetValue(id = R.id.cb_googleplus, value = "GP"),
            @WidgetValue(id = R.id.cb_twitter, value = "TW")
        })
    public List<String> sns;
}
```

If you choose check boxes "Facebook" and "Google+", then the value of
the `DefaultForm#sns` will be `List<String>` which has 2 items "FB" and "GP".
Note that `@Widget` is used just for grouping same kind of `CheckBox`es,
so the other `ViewGroup`'s subclasses than `LinearLayout` like `RelativeLayout`
can be related to `@Widget`.

## Spinner
If you use `<Spinner>`, then just use `@Widget` for the related field of the Form class.
For example, if the part of your `res/layout/some_layout.xml` is like this:

```xml
<Spinner
    android:id="@+id/spn_credit_card_type"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

then you should define your Form class like this:

```java
public class DefaultForm {
    @Widget(id = R.id.spn_credit_card_type)
    public String creditCardType;
}
```

If you want to use the head item as a dummy text like "Please select",
then set `Widget#headIsDummy` to `true` and add `@Required` annotation.
Then you can check whether the user selected other than the head item or not.


Validations
===

Following validation classes are available:

1. RequiredValidator
    * Validates the EditText's value is not `null` nor empty string.
    * The target fields must have field in Form class with `@Required`.
    * If you need value with some conditions, use `@When` as follows:
        ```java
        public class CustomRequiredWhenForm {
            @Widget(id = R.id.spn_reason, nameResId = R.string.form_custom_required_when_reason)
            public String reason;

            @Widget(id = R.id.textfield_reason_other, nameResId = R.string.form_custom_required_when_reason_other,
                    validateAfter = R.id.spn_reason)
            @Required(when = {
                @When(id = R.id.spn_reason, equalsTo = "2")
            })
            public String reasonOther;
        }
        ```
      If the user select the 3rd option from the spinner `R.id.spn_reason` and
      the user does not input text field `R.id.textfield_reason_other`, then this validator
      assume it is an error.
1. IntTypeValidator
    * Validates the EditText's value whether it is a valid integer format.
    * The target fields must have field in Form class with `@IntType`.
1. MaxValueValidator
    * Validates the EditText's value whether it is less than or equals to
      the specified value.
    * The target fields must have field in Form class with `@MaxValue`.
1. MinValueValidator
    * Validates the EditText's value whether it is more than or equals to
      the specified value.
    * The target fields must have field in Form class with `@MinValue`.
1. IntRangeValidator
    * Validates the EditText's value whether it is in the specified range:
      the minimum value is `IntRange#min()` and the maximum value is `IntRange#max()`.
    * The target fields must have field in Form class with `@IntRange`.
1. DigitsValidator
    * Validates the EditText's value whether it consists only of digits (which means 0 through 9).
    * The target fields must have field in Form class with `@Digits`.
1. HiraganaValidator
    * Validates the EditText's value whether it consists only of Japanese Hiragana.
    * The target fields must have field in Form class with `@Hiragana`.
1. KatakanaValidator
    * Validates the EditText's value whether it consists only of Japanese Katakana.
    * The target fields must have field in Form class with `@Katakana`.
1. SinglebyteValidator
    * Validates the EditText's value whether it consists only of single-byte characters.
    * The target fields must have field in Form class with `@Singlebyte`.
1. MultibyteValidator
    * Validates the EditText's value whether it consists only of multi-byte characters.
    * The target fields must have field in Form class with `@Multibyte`.
1. LengthValidator
    * Validates the EditText's value whether it has the specified length.
    * The target fields must have field in Form class with `@Length`.
1. MaxLengthValidator
    * Validates the EditText's value whether its length is less than or equals to
      the specified length.
    * The target fields must have field in Form class with `@MaxLength`.
1. NumOfDigitsValidator
    * Validates the EditText's value whether its length is the specified length.
    * The target fields must have field in Form class with `@NumOfDigits`.
    * This validator resembles to the LengthValidator, but this validator
      does not treat as an error if the value includes non-digit character even
      though its length does not match `NumOfDigits#value()`.
1. MaxNumOfDigitsValidator
    * Validates the EditText's value whether its length is less than or equals to
      the specified length.
    * The target fields must have field in Form class with `@MaxNumOfDigits`.
    * This validator resembles to the MaxLengthValidator, but this validator
      does not treat as an error if the value includes non-digit character even
      though its length exceeds `MaxNumOfDigits#value()`.
1. DatePatternValidator
    * Validates the EditText's value with date format.
    * The target fields must have field in Form class with `@DatePattern`.
    * Date format used in validation is `java.text.DateFormat.SHORT`, which will change with locale.
    * If you want to use custom format, use `DatePattern#value()`.
1. PastDateValidator
    * Validates the EditText's value is valid date format and past date.
    * The target fields must have field in Form class with `@PastDate`.
    * Date format used in validation is `java.text.DateFormat.SHORT`, which will change with locale.
    * If you want to use custom format, use `@DatePattern` annotation together.
      PastDateValidator uses `DatePattern#value()`.
    * If you do not want to assume today as an error, set `PastDate#allowToday` to `true`.
1. EmailValidator
    * Validates the EditText's value whether it is e-mail format.
    * The target fields must have field in Form class with `@Email`.
    * Pattern to be used in validation is this: `^[\\w-]+(\\.[\\w-]+)*@([\\w][\\w-]*\\.)+[\\w][\\w-]*$`
    * If you want to use custom format, define your format in your style
      using `afeValidatorDefinitions` and `afeCustomEmailPattern`.
1. RegexValidator
    * Validates the EditText's value whether it matches the specified regular expression.
    * The target fields must have field in Form class with `@Regex`.
    * Regular expression must be specified by `Regex#value()`.


Orders of the validation
===

Orders of the validation for each items are defined by `Widget#validateAfter`.
For example, if you define form like following, the validators validate `name` at first,
then validate `age`.
Note that they are different from the orders in the screen.

```java
public class DefaultForm {
    @Widget(id = R.id.textfield_name)
    @Required
    public String name;

    @Widget(id = R.id.textfield_age, validateAfter = R.id.textfield_name)
    @IntType
    public String age;
}
```


Customizations
===

You can customize the behaviours and messages like following:

1. Stop policy

    Stop policy controls the validators to continue or stop
    when they detected errors.
    For example, if you want to validate all the items and show all the errors,
    you should define your theme like this:

    ```xml
    <style name="YourTheme">
        <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
    </style>

    <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
        <item name="afeStopPolicy">continueAll</item>
    </style>
    ```

1. Available validations

    You can enable or disable the standard validators.
    You can also add your own validator.
    For example, if you want to use only the RequiredValidator,
    you should define your theme like this:

    ```xml
    <string-array name="your_standard_validators">
        <item>com.androidformenhancer.validator.RequiredValidator</item>
    </string>

    <style name="YourTheme">
        <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
    </style>

    <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
        <item name="afeStandardValidators">@array/your_standard_validators</item>
    </style>
    ```

1. Validation error messages

    You can override validation error messages.
    For example, if you want to override the error message for the RequiredValidator,
    You should define your theme like this:

    ```xml
    <string name="custom_msg_validation_required">You MUST fill in %1$s!</string>

    <style name="YourTheme">
        <item name="afeValidatorMessages">@style/YourValidatorMessages</item>
    </style>

    <style name="YourValidatorMessages">
        <item name="afeErrorRequired">@string/custom_msg_validation_required</item>
    </style>
    ```

    The name of the item used in the error message is the name of the field in Form class.
    If you want to change name, use annotation's `nameResId` attribute.
    For example, if you define field like:

    ```java
    @Widget(id = R.id.textfield_name)
    @Required
    public String firstName;
    ```

    the error message will be "firstName is required".
    If you define Form like this:

    ```java
    @Widget(id = R.id.textfield_name, nameResId = R.string.first_name)
    @Required
    public String firstName;
    ```

    or

    ```java
    @Widget(id = R.id.textfield_name)
    @Required(nameResId = R.string.first_name)
    public String firstName;
    ```

    and define strings.xml like:

    ```xml
    <string name="first_name">First name</string>
    ```

    then the result will be "First name is required".

1. Error icons

    You can change validation error icons as follows:

    ```xml
    <style name="YourTheme">
        <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
    </style>

    <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
        <item name="afeValidationIconError">@drawable/your_icon_error</item>
        <item name="afeValidationIconOk">@drawable/your_icon_ok</item>
    </style>
    ```


ProGuard
===

If you want to use ProGuard, edit proguard-project.txt.

1. Keep class name of the Validators. This is always required.

        -keep class com.androidformenhancer.validator.* { <init>(...); }

1. Keep class members (public fields) of the Forms and Entities.
   If you use `FormHelper#create()` or `@When`, this is required.

        -keepclassmembers class com.androidformenhancer.sample.demos.DefaultForm {
          public *;
        }
        -keepclassmembers class com.androidformenhancer.sample.demos.DefaultEntity {
          public *;
        }


Samples
===

* Sample applications using this library are included in the samples folder.


Tests
===

* Test codes for JUnit test are included in the tests folder.


Developed By
===

* Soichiro Kashima - <soichiro.kashima@gmail.com>


License
===

    Copyright 2012 Soichiro Kashima

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

