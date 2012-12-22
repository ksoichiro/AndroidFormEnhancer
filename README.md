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

