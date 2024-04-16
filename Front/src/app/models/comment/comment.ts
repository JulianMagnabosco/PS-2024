export interface Comment {
  id:number,
  pub:number,
  userId:number,
   username:string,
   userIconUrl:string,
  text:string,
  fatherText:string,
  childs:Comment[]
}
