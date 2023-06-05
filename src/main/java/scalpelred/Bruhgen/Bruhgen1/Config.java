package scalpelred.Bruhgen.Bruhgen1;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

    public static final ForgeConfigSpec.Builder Builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec Config;

    public static ForgeConfigSpec.ConfigValue<Double> Parabola_a;
    public static ForgeConfigSpec.ConfigValue<Double> Parabola_b;
    public static ForgeConfigSpec.ConfigValue<Double> Parabola_c;

    public static ForgeConfigSpec.ConfigValue<Double> RadialSine_a;
    public static ForgeConfigSpec.ConfigValue<Double> RadialSine_b;
    public static ForgeConfigSpec.ConfigValue<Double> RadialSine_w;
    public static ForgeConfigSpec.ConfigValue<Double> RadialSine_T;

    public static ForgeConfigSpec.ConfigValue<Double> Sine_a;
    public static ForgeConfigSpec.ConfigValue<Double> Sine_b;
    public static ForgeConfigSpec.ConfigValue<Double> Sine_w;
    public static ForgeConfigSpec.ConfigValue<Double> Sine_T;

    public static ForgeConfigSpec.ConfigValue<Integer> ChunkGrid_divider;
    public static ForgeConfigSpec.ConfigValue<Integer> CubeGrid_divider;

    public static ForgeConfigSpec.ConfigValue<Double> HeightByPolarData_A;
    public static ForgeConfigSpec.ConfigValue<Double> HeightByPolarData_B;
    public static ForgeConfigSpec.ConfigValue<Double> HeightByPolarData_C;
    public static ForgeConfigSpec.ConfigValue<Double> HeightByPolarData_D;
    public static ForgeConfigSpec.ConfigValue<Double> HeightByPolarData_E;

    public static ForgeConfigSpec.ConfigValue<Integer> Sections_A;
    public static ForgeConfigSpec.ConfigValue<Integer> Sections_B;
    public static ForgeConfigSpec.ConfigValue<String> Sections_BorderBlock;

    public static ForgeConfigSpec.ConfigValue<Integer> RoundChunks_R;
    public static ForgeConfigSpec.ConfigValue<String> RoundChunks_Filler;

    public static ForgeConfigSpec.ConfigValue<Integer> AverageBlock_coefX;
    public static ForgeConfigSpec.ConfigValue<Integer> AverageBlock_coefY;
    public static ForgeConfigSpec.ConfigValue<Integer> AverageBlock_coefZ;
    public static ForgeConfigSpec.ConfigValue<Boolean> AverageBlock_affectStructures;

    public static ForgeConfigSpec.ConfigValue<Integer> AverageID_coefX;
    public static ForgeConfigSpec.ConfigValue<Integer> AverageID_coefY;
    public static ForgeConfigSpec.ConfigValue<Integer> AverageID_coefZ;
    public static ForgeConfigSpec.ConfigValue<Boolean> AverageID_affectStructures;

    public static ForgeConfigSpec.ConfigValue<Double> ScaledY_coef;
    public static ForgeConfigSpec.ConfigValue<Boolean> ScaledY_fill;

    static {
        Builder.push("BruhGen1");

        Parabola_a = Builder
                .comment(" y = ax^2 + by + c")
                .define("Parabola_a", 0.005);
        Parabola_b = Builder
                .define("Parabola_b", 0.0);
        Parabola_c = Builder
                .define("Parabola_с", 0.0);

        Sine_a = Builder
                .comment(" y = a * sin(wx + T) + b")
                .define("Sine_a", 100.0);
        Sine_b = Builder
                .define("Sine_b", 100.0);
        Sine_w = Builder
                .define("Sine_w", 0.03141592);
        Sine_T = Builder
                .define("Sine_T", 0.01570796);

        RadialSine_a = Builder
                .comment(" y = a * sin(wR + T) + b")
                .define("RadialSine_a", 100.0);
        RadialSine_b = Builder
                .define("RadialSine_b", 100.0);
        RadialSine_w = Builder
                .define("RadialSine_w", 0.03141592);
        RadialSine_T = Builder
                .define("RadialSine_T", 0.0);

        ChunkGrid_divider = Builder
                .comment(" (chunkX % div == 0) && (chunkZ % div == 0)")
                .define("ChunkGrid_divider", 2);
        CubeGrid_divider = Builder
                .comment(" (chunkX % div == 0) && (chunkZ % div == 0) && ((Y / 16) % div == 0")
                .define("CubeGrid_divider", 2);

        HeightByPolarData_A = Builder
                .comment(" y = A * (length + B) + C * (angle + D) + E")
                .define("HeightByPolarData_A", 1.0);
        HeightByPolarData_B = Builder
                .define("HeightByPolarData_B", 0.0);
        HeightByPolarData_C = Builder
                .define("HeightByPolarData_C", 64.0);
        HeightByPolarData_D = Builder
                .define("HeightByPolarData_D", Math.PI);
        HeightByPolarData_E = Builder
                .define("HeightByPolarData_E", 0.0);

        Sections_A = Builder
                .comment(" (x % a == 0) || (z % b == 0)")
                .define("Sections_A", 16);
        Sections_B = Builder
                .define("Sections_B", 16);
        Sections_BorderBlock = Builder
                .define("Sections_BorderBlock", Blocks.OBSIDIAN.getRegistryName().toString());

        RoundChunks_R = Builder
                .comment(" NOTE: it fills entire column (from -64 to 320)")
                .define("RoundChunks_R", 7);
        RoundChunks_Filler = Builder
                .define("RoundChunks_Filler", Blocks.AIR.getRegistryName().toString());

        AverageBlock_coefX = Builder
                .comment(" It's recommended to set only powers of 2")
                .defineInRange("AverageBlock_coefX", 2, 1, 16);
        AverageBlock_coefY = Builder
                .defineInRange("AverageBlock_coefY", 2, 1, 16);
        AverageBlock_coefZ = Builder
                .defineInRange("AverageBlock_coefZ", 2, 1, 16);
        AverageBlock_affectStructures = Builder
                .define("AverageBlock_affectStructures", Boolean.TRUE);

        ScaledY_coef = Builder
                .comment(" y *= coef")
                .defineInRange( "ScaledY_coef", 2.0, 0.0, Double.MAX_VALUE);
        ScaledY_fill = Builder
                .comment(" If enabled, fills empty spaces between neighbourhood Ys")
                .define( "ScaledY_fill", Boolean.TRUE);

        Builder.pop();
        Config = Builder.build();
    }

}

