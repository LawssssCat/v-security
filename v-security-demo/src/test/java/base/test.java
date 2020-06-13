package base;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Test;

import javax.sound.midi.Soundbank;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/6/3 7:34
 */
public class test {
    /**
     * 父亲私有变量是否可被覆盖
     */
    @Test
    public void ss() {
        so son = new so();
        son.show();
        son.show4Son();
    }
}

class fa{
    private String name = "father" ;
    public void show() {
        System.out.println(name);
        System.out.println(this.name);
    }
}

class so extends  fa {
    private String name = "son";
    public void show4Son() {
        System.out.println(name);
        System.out.println(this.name);
    }
}