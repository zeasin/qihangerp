package cn.qihangerp.common.exception.file;

import cn.qihangerp.common.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author qihang
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
