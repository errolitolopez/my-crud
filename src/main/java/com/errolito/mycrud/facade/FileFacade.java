package com.errolito.mycrud.facade;

import com.errolito.mycrud.dto.FileProxyResponse;
import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.dto.FileRequest;
import com.errolito.mycrud.dto.FileResponse;
import com.errolito.mycrud.shared.BaseCrudFacade;

public interface FileFacade extends BaseCrudFacade<Integer, FileQuery, FileRequest, FileResponse> {

    FileProxyResponse getFileProxy(Integer id);
}
