social-leaderboard
==================

An android library project that can be easily integrated in to your game. Illustrates how to use App42 leaderboard cloud APIs.

Steps for integration
Prerequisites

- Add the given App42Leaderboard and Facebook library projects to your workspace

Setting up your Android project

- Go to your Game's project properties and add the two library projects.

- Add the following activities to your application's manifest file

<activity

             android:name="com.shephertz.app42leaderboard.LaunchActivity"

             android:theme="@android:style/Theme.NoTitleBar"

android:configChanges="orientation" >

         </activity>

    <activity

             android:name="com.shephertz.app42leaderboard.LeaderBoardActivity"

             android:theme="@android:style/Theme.NoTitleBar"

android:configChanges="orientation" >

         </activity>       

<activity

             android:name="com.shephertz.app42leaderboard.FriendsActivity"

             android:theme="@android:style/Theme.NoTitleBar"

android:configChanges="orientation" >

         </activity>  

<activity

             android:name="com.shephertz.app42leaderboard.PostGamePlayActivity"

             android:theme="@android:style/Theme.NoTitleBar"

android:configChanges="orientation" >

         </activity>

<activity

             android:name="com.shephertz.app42leaderboard.WallActivity"

             android:theme="@android:style/Theme.NoTitleBar"

android:configChanges="orientation" >

         </activity>



- Add given app42_leaderboard_icon.png files to your Game's res directory.



Using the APIs

1) To launch the App42Leaderboard UI, you need to use



/*

* Launches the app42 connect activity. 

* callerContext is the context of the current activity

* gameName is the name of the game whose leader board is to

* be shown post connect.

*/

public static void launchApp42Connect(Context callerContext,String gameName)



     eg: App42Connect.launchApp42Connect(pContext, "Maze"); 



2) When a game play gets over, you want to submit the users score. You simply need to use 



/*

* Submit the score of the user

* callerContext is the context of the current activity

* score is the value of the points scored by the user in the game

* gameName is the name of the game in which the score was made

* caller is the object which will be used to invoke the result of the operation

*/

public static void submitScore(Context callerContext, long score, String gameName, App42ResultHandler caller)

  

           eg:    App42Connect.submitScore(getContext(), 150, "Maze", null);
