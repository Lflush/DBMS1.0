import javax.annotation.processing.SupportedAnnotationTypes;

public class constrainTest {
    public static void main(String[] args) {
        //不符合类型约束
        boolean re1 = Constrain.constrain("exampleDb", "exampleTb", "学号", "int", 2021101169);

        //不符合null约束
        boolean re2 = Constrain.constrain("exampleDb", "exampleTb", "学号", "char", null);

        //不符合unique约束
        boolean re3 = Constrain.constrain("exampleDb", "exampleTb", "学号", "char", "2021101145");

        //不符合primary约束
        boolean re4 = Constrain.constrain("exampleDb", "exampleTb", "学号", "char", null);

        //全都符合
        boolean re5 = Constrain.constrain("exampleDb", "exampleTb", "学号", "char", "2021101165");

        System.out.println(re1 + " " + re2 + " " + re3 + " " + re4 + " " + re5);
    }
}
