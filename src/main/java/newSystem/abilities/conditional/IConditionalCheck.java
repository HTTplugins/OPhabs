package newSystem.abilities.conditional;

public interface IConditionalCheck
{
    boolean check();
    default void onCheckSuccess() {};
    default void onCheckFail() {};
}
