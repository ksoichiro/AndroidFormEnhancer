AndroidFormEnhancer
===

AndroidFormEnhancerは、Androidアプリケーションで入力フォームを簡単に実装するためのライブラリです。
アノテーションを利用して、入力フォームに関する定義を簡潔に記述することができ、
ActivityやFragmentの中に含まれる画面との値のやり取りや入力チェックのコードを削減することができます。


インストール
===

libraryフォルダがライブラリ本体です。EclipseなどのIDEでAndroid Library Projectとして取り込んでください。


使い方
===

1.  入力フォーム用のPOJOクラスを作成し、アノテーションでフォームの仕様を定義します。

        public class DefaultForm {
            @Required
            @Text(R.id.textfield_name)
            @Order(1)
            public String name;

            @IntType
            @Text(R.id.textfield_age)
            @Order(2)
            public String age;
        }

1.  入力値を文字列以外のエンティティとして使用したい場合は、同名のフィールドを持つエンティティクラスを用意します。

        public class DefaultForm {
            public String name;

            public int age;
        }

1.  ActivityやFragmentに下記のようなコードを書いて、画面から入力値を取り出すところから入力チェック、型変換を行ないます。

        // フォーム用のPOJOクラスオブジェクトへ入力内容を抽出します
        FormHelper<DefaultForm> helper = new FormHelper<DefaultForm>();
        DefaultForm form = helper.extractFormFromView(this, DefaultForm.class);

        // 入力チェックします
        ValidationManager validationManager = new ValidationManager(this);
        ArrayList<String> errorMessages = validationManager.validate(form);

        if (errorMessages.size() > 0) {
            // エラーメッセージを表示します
            Toast.makeText(
                    this,
                    StringUtils.serialize(errorMessages),
                    Toast.LENGTH_SHORT).show();
        } else {
            // entityは入力チェック・型変換の済んだオブジェクトです
            DefaultEntity entity = helper.createEntityFromForm(DefaultEntity.class);
        }


検証クラス
===

以下の検証クラスが利用できます。

1. DatePatternValidator

    * EditTextの値が日付形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@DatePattern`アノテーションを付与されている必要があります。
    * 日付形式には`java.text.DateFormat.SHORT`が使用されます。これはロケールにより変化します。
    * 検証に使用する日付形式をカスタマイズする場合は、`DatePattern#value()`を使用してください。

1. DigitsValidator

    * EditTextの値が半角数字のみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Digits`アノテーションを付与されている必要があります。

1. EmailValidator

    * EditTextの値がEメールアドレスの形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Email`アノテーションを付与されている必要があります。
    * 検証に使用されるEメールアドレスの形式(正規表現)は次の通りです: `^[\\w-]+(\\.[\\w-]+)*@([\\w][\\w-]*\\.)+[\\w][\\w-]*$`
    * 検証に使用するEメールアドレスの形式をカスタマイズする場合は、
      `afeValidatorDefinitions`と`afeCustomEmailPattern`を使用してstyleに形式を定義してください。

1. HiraganaValidator

    * EditTextの値が全角ひらがなのみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Hiragana`アノテーションを付与されている必要があります。

1. IntRangeValidator

    * EditTextの値が指定の範囲にあることを検証します。
      最小値は`IntRange#min()`、最大値は`IntRange#max()`で指定します。
    * 対象フィールドは、Formクラスに定義され`@IntRange`アノテーションを付与されている必要があります。

1. IntTypeValidator

    * EditTextの値が整数値の形式であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@IntType`アノテーションを付与されている必要があります。

1. KatakanaValidator

    * EditTextの値が全角カタカナのみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Katakana`アノテーションを付与されている必要があります。

1. LengthValidator

    * EditTextの値が指定の文字数であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Length`アノテーションを付与されている必要があります。

1. MaxLengthValidator

    * EditTextの値が指定の値以下であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MaxLength`アノテーションを付与されている必要があります。

1. MaxNumOfDigitsValidator

    * EditTextの値が指定の桁数以下であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MaxNumOfDigits`アノテーションを付与されている必要があります。
    * この検証クラスはMaxLengthValidatorと似ていますが、この検証クラスでは
      `MaxNumOfDigits#value()`で指定する桁数(文字数)を超えていても、
      半角数字以外の文字が含まれていた場合にはエラーとして扱いません。

1. MaxValueValidator

    * EditTextの値が指定の値以下であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MaxValue`アノテーションを付与されている必要があります。

1. MinValueValidator

    * EditTextの値が指定の値以上であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@MinValue`アノテーションを付与されている必要があります。

1. MultibyteValidator

    * EditTextの値がマルチバイト文字列であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Multibyte`アノテーションを付与されている必要があります。

1. NumOfDigitsValidator

    * EditTextの値が指定の桁数であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@NumOfDigits`アノテーションを付与されている必要があります。
    * この検証クラスはLengthValidatorと似ていますが、この検証クラスでは
      `NumOfDigits#value()`で指定する桁数(文字数)と一致していなくても、
      半角数字以外の文字が含まれていた場合にはエラーとして扱いません。

1. PastDateValidator

    * EditTextの値が正しい日付形式であり、過去の日付であることを検証します。
    * 対象フィールドは、Formクラスに定義され`@PastDate`アノテーションを付与されている必要があります。
    * 日付形式には`java.text.DateFormat.SHORT`が使用されます。これはロケールにより変化します。
    * 検証に使用する日付形式をカスタマイズする場合は、`@DatePattern`アノテーションを併せて使用してください。
      PastDateValidatorは`DatePattern#value()`を日付形式として使用します。
    * 今日の日付をエラーとみなしたくない場合は、`PastDate#allowToday`を`true`に設定してください。

1. RegexValidator

    * EditTextの値が指定の正規表現にマッチすることを検証します。
    * 対象フィールドは、Formクラスに`@Regex`アノテーションを付与されている必要があります。
    * 検証に使用する正規表現は`Regex#value()`で指定します。

1. RequiredValidator

    * EditTextの値が`null`や空文字列でないことを検証します。
    * 対象フィールドは、Formクラスに定義され`@Required`アノテーションを付与されている必要があります。

1. SinglebyteValidator

    * EditTextの値がシングルバイト文字のみで構成されていることを検証します。
    * 対象フィールドは、Formクラスに定義され`@Singlebyte`アノテーションを付与されている必要があります。


検証の順序
===

各項目の検証順序は、`@Order`アノテーションを使用して定義します。
例えば、以下のように定義した場合は、`name`、`age`の順番に検証されます。
画面の表示順とは異なることに注意してください。

        public class DefaultForm {
            @Required
            @Text(R.id.textfield_name)
            @Order(1)
            public String name;

            @IntType
            @Text(R.id.textfield_age)
            @Order(2)
            public String age;
        }


カスタマイズ
===

ライブラリの挙動やメッセージは、以下のようにカスタマイズすることができます。

1. 停止ポリシー

    検証クラスがエラーを検出したときに、そのまま続行するか停止するかを制御します。
    例えば、エラーを検出しても全項目を検証し、全てのエラーを表示したい場合は
    以下のようにテーマを定義します。

        <style name="YourTheme">
            <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
        </style>

        <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
            <item name="afeStopPolicy">continueAll</item>
        </style>

1. 利用可能な検証クラス

    標準で利用可能な検証クラスは、有効/無効を切り替えることができ、
    独自の検証クラスを追加することもできます。
    例えば、RequiredValidatorだけを有効にしたい場合は、以下のようにテーマを定義します。

        <string-array name="your_standard_validators">
            <item>com.androidformenhancer.validator.RequiredValidator</item>
        </string>

        <style name="YourTheme">
            <item name="afeValidatorDefinitions">@style/YourValidatorDefinitions</item>
        </style>

        <style name="YourValidatorDefinitions" parent="@style/AfeDefaultValidators">
            <item name="afeStandardValidators">@array/your_standard_validators</item>
        </style>

1. 検証エラーメッセージ

    検証エラーメッセージは上書きすることができます。
    例えば、RequiredValidatorのエラーメッセージを上書きしたい場合は
    以下のようにテーマを定義します。

        <string name="custom_msg_validation_required">%1$sは絶対に入力してください！</string>

        <style name="YourTheme">
            <item name="afeValidatorMessages">@style/YourValidatorMessages</item>
        </style>

        <style name="YourValidatorMessages">
            <item name="afeErrorRequired">@string/custom_msg_validation_required</item>
        </style>

    エラーメッセージに含める項目名は、デフォルトではFormクラスに定義するフィールド名が使用されます。
    この項目名を変更したい場合は、アノテーションの`nameResId`属性を使用してください。
    例えば、以下のようにフィールドを定義します。

        @Required
        public String firstName;

    この場合、エラーメッセージは「firstNameは必ず入力してください」となります。
    以下のように定義し、

        @Required(nameResId = R.string.first_name)
        public String firstName;

    strings.xmlを次のように記述した場合

        <string name="first_name">お名前(名)</string>

    エラーメッセージは「お名前(名)は必ず入力してください」となります。



ProGuard
===

ProGuardを使用する場合は、以下のようにproguard-project.txtを編集してください。

1. Validatorのクラス名を維持します。これは常に必須です。

        -keep class com.androidformenhancer.validator.* { <init>(...); }

1. FormクラスとEntityクラスのメンバー(publicなフィールド)名を維持します。
   FormHelper#createEntityFromForm()を使用する場合は必須です。

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

