<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Insert title here</title>
<script  type="text/javascript">
    var page = parseInt(1);//第几页开始
    var maxPage = parseInt(377);//第几页结束
    var content = "\uFEFF";
    function getInfo(){ 
        alert(123);
         todo();
     }
     
     function todo(){
         try{
         $.get("http://www.mabangerp.com/index.php?mod=order.orderSearch",{"page":page},
         function(data,status){
             const theData = JSON.parse(data);
             console.log("加载第",page,"页数据",",共"+(theData.pageCount/50)+"页",status);
             if(status == "success"){
                 const orders = theData.orderDataList;
                 orders.forEach(function(order){
                     order.orderItem_data_list.forEach(function(sku){
                         if(sku.warehouseName == "上海自建仓-澄建路"){
                             content += ([""+order.salesRecordNumber,sku.stockSku,sku.quantity,sku.hasGoods==1?'有货':'没货'].join(','));
                             content += "\n";
                         }
                     });
                 });                
                 if(page &lt; maxPage){
                     page++;
                     todo();
                 }else{
                     console.log("数据抓取完成，开始下载文件");
                     var csvData = new Blob([content], { type: 'text/csv' });
                     var a = document.createElement('a');
                     a.href = URL.createObjectURL(csvData);
                     a.target = '_blank';
                     a.download = 'info.csv';
                     document.body.appendChild(a);
                     console.log(a);
                     a.click();
                     document.body.removeChild(a);
                 } 
             }
         });
         }catch(e){
             console.log(e);
         }
        } 
</script>
</head>
<body>
<input type="button" onclick="getInfo()" value="请点击"/>
</body>
</html>