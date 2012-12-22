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

1.  入力値を文字列以外のエンティティとして使用したい場合は、同名のフィールドを持つエンティティクラスを用意します。

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

