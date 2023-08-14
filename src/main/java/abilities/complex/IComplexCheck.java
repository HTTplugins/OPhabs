package abilities.complex;

public interface IComplexCheck
{
    default boolean tryPreCheck()
    {
        return true;
    }

    default void onPreCheckFail() {}

    default void onPreCheckSuccess() {}

    default boolean tryPostCheck()
    {
        return true;
    }

    default void onPostCheckFail() {}

    default void onPostCheckSuccess() {}
}
