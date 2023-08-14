package registry;

public interface IRegistry
{
    default void initRegistry() {};
    void loadRegistry();
    void saveRegistry();
}
