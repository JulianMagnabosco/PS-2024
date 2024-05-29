export interface Comment {
  id:number,
  pub:number,
  dateTime:string,
  userId:number,
   username:string,
   userIconUrl:string,
  text:string,
  idFather:number,
  fatherText:string,
  children:Comment[],
  deleted:boolean
}
