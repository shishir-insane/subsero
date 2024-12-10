package com.sr.subsero.subscription_category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubscriptionCategoryDTO {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    private String description;

    @Size(max = 100)
    private String icon;

}
