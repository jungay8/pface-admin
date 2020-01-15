package com.pface.admin.modules.member.dto;

import com.pface.admin.modules.member.po.FaceSensebox;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FaceSenseboxExDTO extends FaceSensebox implements Serializable {

    private static final long serialVersionUID = 6527326519643792977L;

    private Date minAuthBegindate;

    private Date maxAuthEnddate;

}
