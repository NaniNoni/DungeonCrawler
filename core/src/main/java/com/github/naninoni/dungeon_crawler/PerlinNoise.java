package com.github.naninoni.dungeon_crawler;

import com.badlogic.gdx.math.MathUtils;

/**
 * Perlin noise utility class. DO NOT INSTANTIATE.
 */
public final class PerlinNoise {
    private PerlinNoise() {} // default Private constructor to throw on instantiation

    private static final int[] p = new int[512];

    // Static class initializer
    // Called when the class is loaded
    static {
        initPerms();
    }

    private static void initPerms() {
        int[] basePerm = new int[256];
        for (int i = 0; i < 256; i++) {
            basePerm[i] = i;
        }

        // Fisher-Yates shuffle
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
        for (int i = 255; i > 0; i--) {
            int random = MathUtils.random(i);
            int temp = basePerm[i];
            basePerm[i] = basePerm[random];
            basePerm[random] = temp;
        }

        // Duplicate the array to avoid overflow issues
        // when xi and yi = 255. Then, accessing at xi + 1 or yi + 1 is an overflow.
        for (int i = 0; i < 256; i++) {
            p[i] = basePerm[i];
            p[i + 256] = basePerm[i];
        }
    }


    /**
     * Fade function as defined by Ken Perlin.
     * 6t^5 - 15t^4 + 10t^3
     */
    private static float fade(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * @param hash The hash generated using the permutations array
     * @param x The x coordinate
     * @param y The y coordinate
     * @return a "random" vector from the following 12 vectors:
     * (1,1,0),(-1,1,0),(1,-1,0),(-1,-1,0),
     * (1,0,1),(-1,0,1),(1,0,-1),(-1,0,-1),
     * (0,1,1),(0,-1,1),(0,1,-1),(0,-1,-1)
     */
    private static float grad(int hash, float x, float y) {
        // Take the hashed value and take the first 3 bits of it
        // (h id in the range [0, 7])
        int h = hash & 0b111;
        float u = h < 4 ? x : y;
        float v = h < 4 ? y : x;

        // Use the last 2 bits to decide if u and v are positive or negative. Then return their addition.
        if ((h & 1) != 0) {
            u = -u;
        }
        if ((h & 2) != 0) {
            v = -v;
        }

        return u + v;
    }

    /**
     * Perlin noise generation function.
     * Made with reference to the <a href="https://adrianb.io/2014/08/09/perlinnoise.html">Understanding Perlin Noise</a>) article.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The perlin noise value between 0 and 1
     */
    public static float perlinNoise(float x, float y) {
        // Step 1:
        // Divide x, y coordinates into unit cells.

        // These represent the unit square the out coordinate is in.
        // We bind these in the range [0, 255] to avoid overflow error later.
        int xi = MathUtils.floor(x) & 255;
        int yi = MathUtils.floor(y) & 255;
        /*
        & 255 is actually equivalent to % 256
        & 255 ensures that only the lower 8 bits of the number are kept
        % 256 returns the remainder when a number is divided by 256.

        Example:
        259 = 100000011     (binary representation of 259)
      & 255 =  11111111     (binary representation of 255)
        -----------------
            =  00000011     (binary representation of 3)
         */

        // obtaining fractional part of the coordinates to get varied results
        x = x - MathUtils.floor(x);
        y = y - MathUtils.floor(y);

        // Compute fade curves for x, y
        float u = fade(x);
        float v = fade(y);

        /*
        The Perlin Noise hash function is used to get a unique value for every coordinate input.
        A hash function, as defined by wikipedia, is:
        …any function that can be used to map data of arbitrary size to data of fixed size,
        with slight differences in input data producing very big differences in output data.
         */
        // Hash coordinates of the corners
        int topLeft     = p[p[xi] + yi];
        int topRight    = p[p[xi + 1] + yi];
        int bottomLeft  = p[p[xi] + (yi + 1)];
        int bottomRight = p[p[xi + 1] + (yi + 1)];

        // Adding blended results from corners
        float res = MathUtils.lerp(
            MathUtils.lerp(grad(topLeft, x, y), grad(topRight, x - 1, y), u),
            MathUtils.lerp(grad(bottomLeft, x, y - 1), grad(bottomRight, x - 1, y - 1), u),
            v
        );

        // Scale result to [0,1]
        return (res + 1) / 2;
    }
}
