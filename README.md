# Sleep Rating
**Sleep Rating** is an Android App that helps you collect information about your sleep.

It stores every sleep duration in a local dataBase; then this records can be observed and analysed in the chart📊 section.

As a user, you have the option to set up a notification alarm; just to remind you to rate your sleep💤 period.

Here is a short video presentation.

[![App demo](https://img.youtube.com/vi/_6Xt2AG5Uec/0.jpg)](https://youtu.be/_6Xt2AG5Uec)

*This app demonstrates the following views and libraries:*

* Room database
* DAO
* Coroutines
* MPAndroidChart
* LeakCanary

*It also uses and builds on the following techniques:*

* Transformation map
* Data Binding in XML files
* ViewModel Factory
* Using Backing Properties to protect MutableLiveData
* Observable state LiveData variables to trigger navigation

<img src="mvvm.png">

Thanks to LeakCanary’s knowledge of the internals of the Android Framework, **Sleep Rating** has been tested for memory leaks:

<img src="leakCanary_report.png">
