package com.test.mongodb.controller.result;

import com.test.common.response.ResponseResult;
import com.test.common.response.ResultCode;
import com.test.mongodb.entity.CmsPage;
import lombok.Data;

@Data
public class CmsPageResult extends ResponseResult {

  CmsPage cmsPage;

  public CmsPageResult(ResultCode resustCode,CmsPage cmsPage) {
    super(resustCode);
    this.cmsPage = cmsPage;
  }

}
