package tehnut.gourmet.core.data;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Type;
import java.util.function.Predicate;

@JsonAdapter(BushGrowth.Serializer.class)
public class BushGrowth {

    public static final BushGrowth DEFAULT = new BushGrowth(3, 9);

    private final int maxProduce;
    private final Predicate<Integer> lightCheck;
    // For serialization purposes
    private final transient int minLight;
    private final transient int maxLight;

    public BushGrowth(int maxProduce, int minLight, int maxLight) {
        this.maxProduce = maxProduce;
        this.lightCheck = light -> light >= minLight && light <= maxLight;
        this.minLight = minLight;
        this.maxLight = maxLight;
    }

    public BushGrowth(int maxProduce, int minLight) {
        this(maxProduce, minLight, 15);
    }

    public int getMaxProduce() {
        return maxProduce;
    }

    public boolean checkLight(int lightLevel) {
        return lightCheck.test(lightLevel);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public static class Serializer implements JsonSerializer<BushGrowth>, JsonDeserializer<BushGrowth> {
        @Override
        public BushGrowth deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = element.getAsJsonObject();
            int maxProduce = json.has("maxProduce") ? json.getAsJsonPrimitive("maxProduce").getAsInt() : 15;
            int minLight = json.has("minLight") ? json.getAsJsonPrimitive("minLight").getAsInt() : 8;
            int maxLight = json.has("maxLight") ? json.getAsJsonPrimitive("maxLight").getAsInt() : 15;
            return new BushGrowth(maxProduce, minLight, maxLight);
        }

        @Override
        public JsonElement serialize(BushGrowth src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("maxProduce", src.maxProduce);
            object.addProperty("minLight", src.minLight);
            object.addProperty("maxLight", src.maxLight);
            return object;
        }
    }
}
