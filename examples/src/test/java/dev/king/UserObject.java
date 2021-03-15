package dev.king;

import dev.king.universal.processor.annotation.Column;
import dev.king.universal.processor.annotation.PrimaryKey;
import dev.king.universal.processor.annotation.Table;
import dev.king.universal.processor.annotation.Unique;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Table("user_object")
@RequiredArgsConstructor
public class UserObject {

    @Column
    @PrimaryKey
    private final UUID uuid;
    @Column
    @Unique
    private final World world;
    @Column
    @Unique
    private final Player teupai;

}
