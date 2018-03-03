package tehnut.gourmet.core.data;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tehnut.gourmet.core.util.GourmetLog;

import java.lang.reflect.Type;

@JsonAdapter(EatenEffect.Serializer.class)
public final class EatenEffect {

    private final Potion potion;
    private final int amplifier;
    private final int duration;
    private final double chance;

    public EatenEffect(Potion potion, int amplifier, int duration, double chance) {
        this.potion = potion;
        this.amplifier = amplifier;
        this.duration = duration;
        this.chance = chance;
    }

    public Potion getPotion() {
        return potion;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getDuration() {
        return duration;
    }

    public double getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public static class Serializer implements JsonSerializer<EatenEffect>, JsonDeserializer<EatenEffect> {
        @Override
        public EatenEffect deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = element.getAsJsonObject();
            ResourceLocation potionId = new ResourceLocation(json.getAsJsonPrimitive("potion").getAsString());
            int amplifier = json.has("amplifier") ? json.getAsJsonPrimitive("amplifier").getAsInt() : 0;
            int duration = json.has("duration") ? json.getAsJsonPrimitive("duration").getAsInt() : 100;
            double chance = json.has("chance") ? json.getAsJsonPrimitive("duration").getAsDouble() : 1.0D;

            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
            if (potion == null) {
                GourmetLog.DEFAULT.error("Potion with ID " + potionId + " was requested, but it does not exist.");
                return null;
            }

            return new EatenEffect(potion, amplifier, duration, chance);
        }

        @Override
        public JsonElement serialize(EatenEffect src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("potion", src.potion.getRegistryName().toString());
            jsonObject.addProperty("amplifier", src.amplifier);
            jsonObject.addProperty("duration", src.duration);
            jsonObject.addProperty("chance", src.chance);
            return jsonObject;
        }
    }
}
