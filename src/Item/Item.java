package Item;

import org.jetbrains.annotations.NotNull;

public class Item {
    @NotNull
    private String name;

    public Item(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
