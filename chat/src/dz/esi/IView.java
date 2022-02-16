package dz.esi;

public interface IView {
    void init();
    void assemble();
    void style();
    void event();
    default void skin(){
        init();
        assemble();
        style();
        event();
    }
}
