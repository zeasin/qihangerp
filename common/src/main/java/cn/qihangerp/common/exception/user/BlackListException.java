package cn.qihangerp.common.exception.user;

/**
 * 黑名单IP异常类
 * 
 * @author qihang
 */
public class BlackListException extends UserException
{
    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
