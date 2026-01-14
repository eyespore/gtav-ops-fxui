package club.pineclone.gtavops;

public interface AppLifecycleAware {

    default void onAppInit() throws Exception {}

    default void onAppStart() throws Exception {}

    default void onAppStop() throws Exception {}

}
