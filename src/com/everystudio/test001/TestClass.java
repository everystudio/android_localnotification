package com.everystudio.test001;
import com.unity3d.player.UnityPlayer;
import android.util.Log;

public class TestClass {
	   // (1) Unity側から呼び出されて何か処理を行う関数
    public void FuncA(final String str)
    {
        Log.d("Unity Native", str);
    }
    // (2) Unity側に値を返す関数
    // C#とJavaでバイト数が同じ型同士ならやりとり可能。
    // ただし、C#のstring型とJavaのString型など同じ型でも名前が違うものもあるので注意。
    // C#のint, long, bool(Javaだとboolean型)が使用可能。
    public String FuncB(final String str)
    {
        return "Back " + str;
    }
    // (3) Unity側のスクリプトのonCallBack関数を呼び出す関数
    // スクリプトがAttachされたGameObjectの名前を渡し、スクリプト内指定の
    // 関数を呼び出すことが可能。呼び出し先の関数がvoid型の場合は
    // UnityPlayer.UnitySendMessage(gameObjName, "onCallBack", ""); のように記述。
    // 3番目の引数は必ず記述しないとコンパイルエラーになった気がする…。
    public void FuncC(final String gameObjName, final String str)
    {
        UnityPlayer.UnitySendMessage(gameObjName, "onCallBack", str);
    }    
    
}
