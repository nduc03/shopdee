package Item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class Item {
    @NotNull
    private String name;

    @JsonCreator
    public Item(@JsonProperty("name") @NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
