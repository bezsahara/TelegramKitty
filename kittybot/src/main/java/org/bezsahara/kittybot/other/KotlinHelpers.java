package org.bezsahara.kittybot.other;

import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind;
import org.bezsahara.kittybot.telegram.utils.entity.EntityBuilder;
import org.bezsahara.kittybot.telegram.utils.entity.EntityKind;

import java.util.Collection;
import java.util.List;

public class KotlinHelpers {
    public static EntityBuilder append(
            EntityBuilder entityBuilder,
            String string,
            EntityKind[] kinds
    ) {
        entityBuilder.addString(string, kinds);
        return entityBuilder;
    }

    public static int updKindArrayToInt(UpdKind[] values) {
        int bits = 0;

        for (UpdKind value : values) {
            bits |= 1 << value.ordinal;
        }

        return bits;
    }

    public static int updKindArrayToInt(Collection<UpdKind> values) {
        int bits = 0;

        if (values instanceof List<UpdKind> list) {
            for (int i = 0; i < list.size(); i++) {
                bits |= 1 << list.get(i).ordinal;
            }
        } else {
            for (UpdKind value : values) {
                bits |= 1 << value.ordinal;
            }
        }

        return bits;
    }
}
