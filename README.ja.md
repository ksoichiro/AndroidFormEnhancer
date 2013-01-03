AndroidFormEnhancer
===

AndroidFormEnhancerは、Androidアプリケーションで入力フォームを簡単に実装するためのライブラリです。
アノテーションを利用して、入力フォームに関する定義を簡潔に記述することができ、
ActivityやFragmentの中に含まれる画面との値のやり取りや入力チェックのコードを削減することができます。

![Screenshot](https://raw.github.com/ksoichiro/AndroidFormEnhancer/master/samples/images/screenshot.png "Screenshots")


インストール
===

libraryフォルダがライブラリ本体です。EclipseなどのIDEでAndroid Library Projectとして取り込んでください。


使い方
===

1.  入力フォーム用のPOJOクラスを作成し、アノテーションでフォームの仕様を定義します。

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

1.  入力値を文字列以外のエンティティとして使用したい場合は、同名のフィールドを持つエンティティクラスを用意します。

    ```java
    public class DefaultEntity {
        public String name;
        public int age;
    }
    ```

1.  ActivityやFragmentに下記のようなコードを書いて、画面から入力値を取り出すところから入力チェック、型変換を行ないます。

    ```java
    ValidationResult result = new FormHelper(DefaultForm.class, this).validate();
    if (result.hasError()) {
        // エラーメッセージを表示します
        Toast.makeText(this, result.getAllSerializedErrors(), Toast.LENGTH_SHORT).show();
    } else {
        // entityは入力チェック・型変換の済んだオブジェクトです
        DefaultEntity entity = helper.create(DefaultEntity.class);
    }
    ```

1.  もしフォーカスが外れたタイミングで入力チェックしたい場合は、次のように書くだけです。

    ```java
    new FormHelper(DefaultForm.class, this).setOnFocusOutValidation();
    ```

    ただし、これはテキストのフィールドだけに有効な方法です。


入力値の取得
===

レイアウトから入力値を取得するには、まずFormクラスを作成します。
Formクラスはpublicなフィールドを持つだけの単なるPOJOクラスです。
全てのフィールドはpublicで、`String`か`java.util.List<String>`型である必要があります。

```java
public class DefaultForm {
    public String name;
}
```

各フィールドは、`android.widget.EditText`のようなウィジェットと関連付ける必要がります。
ウィジェットとの関連付けをするには、特別なアノテーションをフィールドに付与します。

## EditText
`<EditText>`タグを使う場合は、`@Widget`をFormクラスのフィールドに付与します。
例えば、`res/layout/some_layout.xml`の一部が以下の通りだとします。

```xml
<EditText
    android:id="@+id/textfield_name"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

この場合、Formクラスの定義は次のようにします。

```java
public class DefaultForm {
    @Widget(id = R.id.textfield_name)
    public String name;
}
```

## RadioGroupとRadioButton
`<RadioGroup>`タグや`<RadioButton>`を使い、いずれかのラジオボタンが選択されていることを
検証したい場合は、`@Widget`と`@WidgetValue`のアノテーションをFormクラスのフィールドに付与します。
例えば、`res/layout/some_layout.xml`の一部が以下の通りだとします。

```xml
<RadioGroup
    android:id="@+id/rg_gender"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <RadioButton
        android:id="@+id/rb_male"
        android:text="男性"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <RadioButton
        android:id="@+id/rb_female"
        android:text="女性"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</RadioGroup>
```

この場合、Formクラスの定義は次のようにします。

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

もし"男性"のラジオボタンを選択すれば、`DefaultForm#gender`の値は"M"になります。

## CheckBox
`<CheckBox>`タグを使用し、少なくとも1つ以上のチェックボックスがチェックされている
ことを検証したい場合は、`@Widget`と`@WidgetValue`アノテーションを
Formクラスのフィールドに付与します。
例えば、`res/layout/some_layout.xml`の一部が以下の通りだとします。

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

この場合、Formクラスの定義は次のようにします。

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

もし"Facebook"と"Google+"のチェックボックスを選択したならば、
`DefaultForm#sns`の値は"FB"と"GP"の2つの要素を持つ`List<String>`になります。
`@Widget`は、単に同じ種類の`CheckBox`をグループ化するためだけに使用しています。
この例にある`LinearLayout`だけでなく、他の`ViewGroup`のサブクラスである
`RelativeLayout`なども`@Widget`と関連付けられることに注意してください。

## Spinner
`<Spinner>`タグを使う場合は、`@Widget`アノテーションをFormクラスのフィールドに付与します。
例えば、`res/layout/some_layout.xml`の一部が以下の通りだとします。

```xml
<Spinner
    android:id="@+id/spn_credit_card_type"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

この場合、Formクラスの定義は次のようにします。

```java
public class DefaultForm {
    @Widget(id = R.id.spn_credit_card_type, Widget.Type.SPINNER)
    public String creditCardType;
}
```

もし、先頭の要素を"選択してください"のようなダミー文字列として使いたい場合は、
`Widget#headIsDummy`を`true`に設定し、`@Required`アノテーションをフィールドに追加します。
これにより、先頭の要素以外が選択されているかどうかを検証することができます。


検証クラス
===

以下の検証クラスが利用できます。

1. RequiredValidator
    * EditTextの値が`null`や空文字列でないことを検証します。
    * 対象フィールドは、Formクラスに定義され`@Required`アノテーションを付与されている必要があります。
    * 条件付きでチェックした場合は、次のように`@When`を併せて使います。
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
      もしユーザが`R.id.spn_reason`のSpinnerから3番目の選択肢を選び、
      ユーザが`R.id.textfield_reason_other`のテキストフィールドに入力しなかった場合、
      この検証クラスはエラーとみなします。
1. IntTypeValidator
    * EditTextの値が整数値(Integer)の形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@IntType`アノテーションを付与されている必要があります。
1. FloatTypeValidator
    * EditTextの値が浮動小数点数(Float)の形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@FloatType`アノテーションを付与されている必要があります。
1. MaxValueValidator
    * EditTextの値が指定の値以下であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MaxValue`アノテーションを付与されている必要があります。
1. MinValueValidator
    * EditTextの値が指定の値以上であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MinValue`アノテーションを付与されている必要があります。
1. IntRangeValidator
    * EditTextの値が指定の範囲にあることを検証します。
      最小値は`IntRange#min()`、最大値は`IntRange#max()`で指定します。
    * 対象フィールドは、Formクラスに定義され`@IntRange`アノテーションを付与されている必要があります。
1. DigitsValidator
    * EditTextの値が半角数字のみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Digits`アノテーションを付与されている必要があります。
1. AlphabetValidator
    * EditTextの値が半角英字のみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Alphabet`アノテーションを付与されている必要があります。
    * もし半角スペースも許可したい場合は、`Alphabet#allowSpace()`を`true`に設定します。
1. AlphaNumValidator
    * EditTextの値が半角英字のみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@AlphaNum`アノテーションを付与されている必要があります。
    * もし半角スペースも許可したい場合は、`AlphaNum#allowSpace()`を`true`に設定します。
1. HiraganaValidator
    * EditTextの値が全角ひらがなのみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Hiragana`アノテーションを付与されている必要があります。
1. KatakanaValidator
    * EditTextの値が全角カタカナのみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Katakana`アノテーションを付与されている必要があります。
1. SinglebyteValidator
    * EditTextの値がシングルバイト文字のみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Singlebyte`アノテーションを付与されている必要があります。
1. MultibyteValidator
    * EditTextの値がマルチバイト文字列であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Multibyte`アノテーションを付与されている必要があります。
1. LengthValidator
    * EditTextの値が指定の文字数であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Length`アノテーションを付与されている必要があります。
1. MaxLengthValidator
    * EditTextの値が指定の値以下であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MaxLength`アノテーションを付与されている必要があります。
1. NumOfDigitsValidator
    * EditTextの値が指定の桁数であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@NumOfDigits`アノテーションを付与されている必要があります。
    * この検証クラスはLengthValidatorと似ていますが、この検証クラスでは
      `NumOfDigits#value()`で指定する桁数(文字数)と一致していなくても、
      半角数字以外の文字が含まれていた場合にはエラーとして扱いません。
1. MaxNumOfDigitsValidator
    * EditTextの値が指定の桁数以下であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MaxNumOfDigits`アノテーションを付与されている必要があります。
    * この検証クラスはMaxLengthValidatorと似ていますが、この検証クラスでは
      `MaxNumOfDigits#value()`で指定する桁数(文字数)を超えていても、
      半角数字以外の文字が含まれていた場合にはエラーとして扱いません。
1. DatePatternValidator
    * EditTextの値が日付形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@DatePattern`アノテーションを付与されている必要があります。
    * 日付形式には`java.text.DateFormat.SHORT`が使用されます。これはロケールにより変化します。
    * 検証に使用する日付形式をカスタマイズする場合は、`DatePattern#value()`を使用してください。
1. PastDateValidator
    * EditTextの値が正しい日付形式であり、過去の日付であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@PastDate`アノテーションを付与されている必要があります。
    * 日付形式には`java.text.DateFormat.SHORT`が使用されます。これはロケールにより変化します。
    * 検証に使用する日付形式をカスタマイズする場合は、`@DatePattern`アノテーションを併せて使用してください。
      PastDateValidatorは`DatePattern#value()`を日付形式として使用します。
    * 今日の日付をエラーとみなしたくない場合は、`PastDate#allowToday`を`true`に設定してください。
1. EmailValidator
    * EditTextの値がEメールアドレスの形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Email`アノテーションを付与されている必要があります。
    * 検証に使用されるEメールアドレスの形式(正規表現)は次の通りです: `^[\\w-]+(\\.[\\w-]+)*@([\\w][\\w-]*\\.)+[\\w][\\w-]*$`
    * 検証に使用するEメールアドレスの形式をカスタマイズする場合は、
      `afeValidatorDefinitions`と`afeCustomEmailPattern`を使用してstyleに形式を定義してください。
1. RegexValidator
    * EditTextの値が指定の正規表現にマッチすることを検証します。
    * 対象フィールドは、Formクラスに`@Regex`アノテーションを付与されている必要があります。
    * 検証に使用する正規表現は`Regex#value()`で指定します。


検証の順序
===

各項目の検証順序は、`Widget#validateAfter`を使用して定義します。
例えば、以下のように定義した場合は、`name`、`age`の順番に検証されます。
画面の表示順とは異なることに注意してください。

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


カスタマイズ
===

ライブラリの挙動やメッセージは、以下のようにカスタマイズすることができます。

1. 停止ポリシー

    検証クラスがエラーを検出したときに、そのまま続行するか停止するかを制御します。
    例えば、エラーを検出しても全項目を検証し、全てのエラーを表示したい場合は
    以下のようにテーマを定義します。

    ```xml
    <style name="YourTheme">
        <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
    </style>

    <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
        <item name="afeStopPolicy">continueAll</item>
    </style>
    ```

1. 利用可能な検証クラス

    標準で利用可能な検証クラスは、有効/無効を切り替えることができ、
    独自の検証クラスを追加することもできます。
    例えば、RequiredValidatorだけを有効にしたい場合は、以下のようにテーマを定義します。

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

1. 検証エラーメッセージ

    検証エラーメッセージは上書きすることができます。
    例えば、RequiredValidatorのエラーメッセージを上書きしたい場合は
    以下のようにテーマを定義します。

    ```xml
    <string name="custom_msg_validation_required">%1$sは絶対に入力してください！</string>

    <style name="YourTheme">
        <item name="afeValidatorMessages">@style/YourValidatorMessages</item>
    </style>

    <style name="YourValidatorMessages">
        <item name="afeErrorRequired">@string/custom_msg_validation_required</item>
    </style>
    ```

    エラーメッセージに含める項目名は、デフォルトではFormクラスに定義するフィールド名が使用されます。
    この項目名を変更したい場合は、アノテーションの`nameResId`属性を使用してください。
    例えば、以下のようにフィールドを定義します。

    ```java
    @Widget(id = R.id.textfield_name)
    @Required
    public String firstName;
    ```

    この場合、エラーメッセージは「firstNameは必ず入力してください」となります。
    項目名をカスタマイズする場合、Formは以下のように定義します。

    ```java
    @Widget(id = R.id.textfield_name, nameResId = R.string.first_name)
    @Required
    public String firstName;
    ```

    もしくは下記の形式です。

    ```java
    @Widget(id = R.id.textfield_name)
    @Required(nameResId = R.string.first_name)
    public String firstName;
    ```

    strings.xmlを次のように記述した場合

    ```xml
    <string name="first_name">お名前(名)</string>
    ```

    エラーメッセージは「お名前(名)は必ず入力してください」となります。

1. エラーアイコン

    入力チェックエラー時に表示されるアイコンは次のように変更することができます。

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

ProGuardを使用する場合は、以下のようにproguard-project.txtを編集してください。

1. Validatorのクラス名を維持します。これは常に必須です。

        -keep class com.androidformenhancer.validator.* { <init>(...); }

1. FormクラスとEntityクラスのメンバー(publicなフィールド)名を維持します。
   `FormHelper#create()`や`@When`を使用する場合は必須です。

        -keepclassmembers class com.androidformenhancer.sample.demos.DefaultForm {
          public *;
        }
        -keepclassmembers class com.androidformenhancer.sample.demos.DefaultEntity {
          public *;
        }


サンプル
===

* ライブラリを使用したサンプルアプリケーションは、samplesフォルダに含まれています。


テスト
===

* JUnitテストコードはtestsフォルダに含まれています。


開発者
===

* Soichiro Kashima - <soichiro.kashima@gmail.com>


ライセンス
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

