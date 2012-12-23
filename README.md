AndroidFormEnhancer
===

AndroidFormEnhancer is a library for implementing input forms easily in the Android application.
You can use annotations to describe the definitions of the input form briefly,
reduce codes that handle data of the screens in the Activities and Fragments.

Install
===

The 'library' folder is the main library.
Please import it into the Eclipse or other IDEs.


Usage
===

1.  Create POJO class of the input form, define public fields and add annotations to them.

1.  If you use types other than String, create an entity class which fields has the same names as
    the form class.

1.  Write the codes like following to the Activity or Fragment to extract data from the screen,
    validate them and convert types.

        // Extract input data into the POJO class object
        FormHelper<DefaultForm> helper = new FormHelper<DefaultForm>();
        DefaultForm form = helper.extractFormFromView(this, DefaultForm.class);

        // Validate input data
        ValidationManager validationManager = new ValidationManager(this);
        ArrayList<String> errorMessages = validationManager.validate(form);

        if (errorMessages.size() > 0) {
            // Show error messages
            Toast.makeText(
                    this,
                    StringUtils.serialize(errorMessages),
                    Toast.LENGTH_SHORT).show();
        } else {
            // This entity object has clean and converted data
            DefaultEntity entity = helper.createEntityFromForm(DefaultEntity.class);
        }


Validations
===

Following validation classes are available:

1. DatePatternValidator
1. DigitsValidator
1. EmailValidator
1. HiraganaValidator
1. IntRangeValidator
1. IntTypeValidator
1. KatakanaValidator
1. LengthValidator
1. MaxLengthValidator
1. MaxNumOfDigitsValidator
1. MaxValueValidator
1. MinValueValidator
1. MultibyteValidator
1. NumOfDigitsValidator
1. PastDateValidator
1. RegexValidator
1. RequiredValidator
1. SinglebyteValidator


Customizations
===

You can customize the behaviours and messages like following:

1. Stop policy

    Stop policy controls the validators to continue or stop
    when they detected errors.
    For example, if you want to validate all the items and show all the errors,
    you should define your theme like this:

        <style name="YourTheme">
            <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
        </style>

        <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
            <item name="afeStopPolicy">continueAll</item>
        </style>

1. Available validations

    You can enable or disable the standard validators.
    You can also add your own validator.
    For example, if you want to use only the RequiredValidator,
    you should define your theme like this:

        <string-array name="your_standard_validators">
            <item>com.androidformenhancer.validator.RequiredValidator</item>
        </string>

        <style name="YourTheme">
            <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
        </style>

        <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
            <item name="afeStandardValidators">@array/your_standard_validators</item>
        </style>

1. Validation error messages

    You can override validation error messages.
    For example, if you want to override the error message for the RequiredValidator,
    You should define your theme like this:

        <string name="custom_msg_validation_required">You MUST fill in %1$s!</string>

        <style name="YourTheme">
            <item name="afeValidatorMessages">@style/YourValidatorMessages</item>
        </style>

        <style name="YourValidatorMessages">
            <item name="afeErrorRequired">@string/custom_msg_validation_required</item>
        </style>


ProGuard
===

If you want to use ProGuard, edit proguard-project.txt.

1. Keep class name of the Validators. This is always required.

        -keep class com.androidformenhancer.validator.* { <init>(...); }

1. Keep class members (public fields) of the Forms and Entities.
   If you use FormHelper#createEntityFromForm(), this is required.

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

