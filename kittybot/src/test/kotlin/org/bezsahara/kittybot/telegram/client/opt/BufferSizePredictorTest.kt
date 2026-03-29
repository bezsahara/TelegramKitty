package org.bezsahara.kittybot.telegram.client.opt

import kotlin.test.Test
import kotlin.test.assertEquals

class BufferSizePredictorTest {

    @Test
    fun initialPredictionUsesRoundedSmallCapacity() {
        val predictor = BufferSizePredictor(
            minCap = 16,
            maxCap = 256,
            initialSmall = 17,
            initialLarge = 18
        )

        assertEquals(32, predictor.decideCapacity())
    }

    @Test
    fun initialPredictionIsClampedToMaxCap() {
        val predictor = BufferSizePredictor(
            minCap = 16,
            maxCap = 100,
            initialSmall = 70,
            initialLarge = 80
        )

        assertEquals(100, predictor.decideCapacity())
    }

    @Test
    fun repeatedOverflowsSwitchPredictionToLargeCapacity() {
        val predictor = freshPredictor()

        predictor.record(actualLen = 33, initialCapacity = 32)
        assertEquals(32, predictor.decideCapacity())

        predictor.record(actualLen = 33, initialCapacity = 32)
        assertEquals(64, predictor.decideCapacity())
    }

    @Test
    fun repeatedFitsAboveSmallCanSwitchPredictionToLargeWithoutOverflow() {
        val predictor = freshPredictor()

        predictor.record(actualLen = 40, initialCapacity = 64)
        assertEquals(32, predictor.decideCapacity())

        predictor.record(actualLen = 40, initialCapacity = 64)
        assertEquals(64, predictor.decideCapacity())
    }

    @Test
    fun smallSamplesPullPredictionBackAndShrinkLargeCapacity() {
        val predictor = BufferSizePredictor(
            minCap = 16,
            maxCap = 512,
            initialSmall = 17,
            initialLarge = 18
        )

        predictor.record(actualLen = 200, initialCapacity = 32)
        predictor.record(actualLen = 200, initialCapacity = 32)
        assertEquals(256, predictor.decideCapacity())

        predictor.record(actualLen = 16, initialCapacity = 256)
        assertEquals(32, predictor.decideCapacity())

        predictor.record(actualLen = 100, initialCapacity = 128)
        assertEquals(128, predictor.decideCapacity())
    }

    @Test
    fun ignoresOutlierSamples() {
        val predictor = freshPredictor()

        predictor.record(actualLen = 10_000, initialCapacity = 32)

        assertEquals(32, predictor.decideCapacity())
    }

    private fun freshPredictor(): BufferSizePredictor =
        BufferSizePredictor(
            minCap = 16,
            maxCap = 256,
            initialSmall = 17,
            initialLarge = 18
        )
}
