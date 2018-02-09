# FunMap    

嘿嘿
//拥有者的userid
    public int ownerId;
    //经纬度
    public double lat;
    public double lng;
    //marker的标题和description
    public String title;
    public String description;
    //marker类型，0为图片marker，1为事件marker
    public int type;
    //用户是否保存此marker
    public boolean isSaved;
    //此marker的图片（数组 0-3张）
    public String[] imgUris;
    //拥有者用户名
    public String userName;
    //拥有者icon
    public String iconUri;
    //开始结束时间unix时间戳
    public long startTime;
    public long endTime;
