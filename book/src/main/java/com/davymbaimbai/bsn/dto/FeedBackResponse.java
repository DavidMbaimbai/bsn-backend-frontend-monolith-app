package com.davymbaimbai.bsn.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedBackResponse {
 private Double note;
 private String comment;
 private boolean ownFeedBack;
}
