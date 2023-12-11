# bpdlh-app
Aplikasi BPDLH FDB

## Tech
- AndroidX
- Kotlin
- MVVM Clean Architecture
- Modularization
- ViewBinding
- RxJava
- Retrofit
- Room
- Glide
- Dagger
- Fast Adapter
- Firebase (Analytic & Crashlytic)

## Build apk
Run command below to generate apk

```bash
./gradlew assembleDebug
./gradlew assembleStaging
./gradlew assembleRelease
```
Run command below to generate and upload apk through firebase app distribution

```bash
./gradlew assembleDebug appDistributionUploadDebug
./gradlew assembleStaging appDistributionUploadStaging
./gradlew assembleRelease appDistributionUploadReleasea
```

You can find the path of apk here
- /app/build/outputs/apk/debug/BPDLHApp-{versionName}-debug.apk
- /app/build/outputs/apk/staging/BPDLHApp-{versionName}-staging.apk
- /app/build/outputs/apk/release/BPDLHApp-{versionName}-release.apk

## Code Convention (IMPORTANT!!!)

if you new member and you want to develop this app, before you must read this and **don't forget all code, you must use english** ,good luck:)

### 1. Naming

taken from ([Android Style Guide - Naming](https://developer.android.com/kotlin/style-guide#naming_2))

- Package Names

Package names are all lowercase, with consecutive words simply concatenated together (no underscores). for example: `com.logeedistribution.core`

- Class names are written in [UpperCamelCase]

For classes that extend an Android component, the name of the class should end with the name of the component; for example: `SignInActivity`, `SignInFragment`, `ImageUploaderService`, `ChangePasswordDialog`.

- Function names are written in [camelCase]

Function names are typically verbs or verb phrases. For example, `sendMessage` or `stop`

- Constant names are written in [UPPER_SNAKE_CASE]

All uppercase letters, with words separated by underscores. for example: `COMMA_JOINER`, `EMPTY_ARRAY`, `NAMES`

- Widget or Layout Ids are written in [lower_snake_case]

All lowercase letters, with words separated by underscores. for example: `@+id/btn_login`, `@+id/view`

### 2. Resources files

Resources file names are written in __lowercase_underscore__.

#### 2.1 Drawable files

Naming conventions for drawables:


| Asset Type   | Prefix            |		Example               |
|--------------| ------------------|-----------------------------|
| Action bar   | `ab_`             | `ab_stacked.9.png`          |
| Button       | `btn_`	            | `btn_send_pressed.9.png`    |
| Dialog       | `dialog_`         | `dialog_top.9.png`          |
| Divider      | `divider_`        | `divider_horizontal.9.png`  |
| Icon         | `ic_`	            | `ic_star.png`               |
| Menu         | `menu_	`           | `menu_submenu_bg.9.png`     |
| Notification | `notification_`	| `notification_bg.9.png`     |
| Tabs         | `tab_`            | `tab_pressed.9.png`         |
| Background   | `bg_`            | `bg_home.png`         |
| Illustration | `il_`            | `il_login.png`         |


Naming conventions for icons (taken from [Android iconography guidelines](http://developer.android.com/design/style/iconography.html)):

| Asset Type                      | Prefix             | Example                      |
| --------------------------------| ----------------   | ---------------------------- |
| Icons                           | `ic_`              | `ic_star.png`                |
| Launcher icons                  | `ic_launcher`      | `ic_launcher_calendar.png`   |
| Menu icons and Action Bar icons | `ic_menu`          | `ic_menu_archive.png`        |
| Status bar icons                | `ic_stat_notify`   | `ic_stat_notify_msg.png`     |
| Tab icons                       | `ic_tab`           | `ic_tab_recent.png`          |
| Dialog icons                    | `ic_dialog`        | `ic_dialog_info.png`         |

Naming conventions for selector states:

| State	       | Suffix          | Example                     |
|--------------|-----------------|-----------------------------|
| Normal       | `_normal`       | `btn_order_normal.9.png`    |
| Pressed      | `_pressed`      | `btn_order_pressed.9.png`   |
| Focused      | `_focused`      | `btn_order_focused.9.png`   |
| Disabled     | `_disabled`     | `btn_order_disabled.9.png`  |
| Selected     | `_selected`     | `btn_order_selected.9.png`  |


#### 3. Layout files

Layout files should match the name of the Android components that they are intended for but moving the top level component name to the beginning. For example, if we are creating a layout for the `SignInActivity`, the name of the layout file should be `activity_sign_in.xml`.

| Component        | Class Name             | Layout Name                   |
| ---------------- | ---------------------- | ----------------------------- |
| Activity         | `UserProfileActivity`  | `activity_user_profile.xml`   |
| Fragment         | `SignUpFragment`       | `fragment_sign_up.xml`        |
| Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml`  |
| AdapterView item | ---                    | `item_person.xml`             |
| Partial layout   | ---                    | `partial_stats_bar.xml`       |

A slightly different case is when we are creating a layout that is going to be inflated by an `Adapter` or we called it `ViewItem`, e.g to populate a `ListView` or `RecyclerView`. In this case, the name of the layout should start with `item_`.
