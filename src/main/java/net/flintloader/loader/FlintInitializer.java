package net.flintloader.loader;

import net.flintloader.loader.api.FlintModule;
import net.flintloader.loader.api.event.client.LateInitEvent;
import net.flintloader.loader.core.event.FlintEventBus;
import net.flintloader.loader.core.event.annot.EventBusListener;

public class FlintInitializer implements FlintModule {

    public static final FlintInitializer INSTANCE = new FlintInitializer();

    @Override
    public void initializeModule() {
        FlintConstants.LOG.info("Initializing Flint API");

        /*ClientTickEvents.START_CLIENT_TICK.register((listener) -> {
            ClientTickEvent event = new ClientTickEvent(listener.level);
            FlintEventBus.INSTANCE.postEvent(event);
        });*/
        // TODO Client Tick

        FlintEventBus.INSTANCE.registerEventListener(FlintInitializer.class);
    }

    @EventBusListener
    public static void lateInitEvent(LateInitEvent event) {
        /*CreativeTabRegistry.getTabs().forEach(tab -> {
            CreativeModeTab finalTab = FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." +
                            tab.getResourceLocation().toString().replace(":", ".")
                    ))
                    .icon(tab.getIcon())
                    .build();

            tab.setTab(finalTab);

            ItemGroupEvents.modifyEntriesEvent(tab.getResourceKey()).register(entries -> CreativeTabRegistry
                    .getTabItems()
                    .stream().filter(t -> t.getLeft().get() == finalTab && t.getRight() != null)
                    .map(Pair::getRight).forEach(itm -> entries.accept(itm.get())));
        });*/
        // TODO Creative Tabs
    }

}