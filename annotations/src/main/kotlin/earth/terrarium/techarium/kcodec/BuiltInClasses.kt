package earth.terrarium.techarium.kcodec

import com.squareup.kotlinpoet.ClassName

private const val RLIB = "com.teamresourceful.resourcefullib"
private const val SERIALIZATION = "com.mojang.serialization"
private const val DATAFIXER = "com.mojang.datafixers"

val CODEC_TYPE = ClassName(SERIALIZATION, "Codec")
val RECORD_CODEC_BUILDER_TYPE = ClassName("$SERIALIZATION.codecs", "RecordCodecBuilder")
val EITHER_TYPE = ClassName("$DATAFIXER.util", "Either")
val CODEC_EXTRAS_TYPE = ClassName("$RLIB.common.codecs", "CodecExtras")
val ENUM_CODEC_TYPE = ClassName("$RLIB.common.codecs", "EnumCodec")
