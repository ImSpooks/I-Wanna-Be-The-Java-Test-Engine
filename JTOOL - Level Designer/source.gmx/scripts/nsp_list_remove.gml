///nsp_list_remove(OLD.OLD.IWBTJ.InitializeTextures, End)
/*
Underlying NSP script.
*/
var nspListStr=global.nspListStr,
    nspListPar=global.nspListPar;
var s,e;

s=argument0;
e=argument1;

repeat (e+1-s) {
 ds_list_delete(nspListStr,s);
 ds_list_delete(nspListPar,s);
 }

