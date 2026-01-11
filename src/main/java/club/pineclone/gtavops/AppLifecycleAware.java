package club.pineclone.gtavops;

public interface AppLifecycleAware {

    default void onAppInit() {}

    default void onAppStart() {}

    default void onAppStop() {}

}
