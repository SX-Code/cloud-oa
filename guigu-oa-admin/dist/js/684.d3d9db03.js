"use strict";(self["webpackChunkguigu_oa_admin"]=self["webpackChunkguigu_oa_admin"]||[]).push([[684],{76684:function(e,t,l){l.d(t,{O:function(){return B},c:function(){return O}});var n=l(73396),o=l(87139);function a(e,t,l,a,i,u){const s=(0,n.up)("QuestionCircleOutlined"),r=(0,n.up)("n-icon"),d=(0,n.up)("n-tooltip"),c=(0,n.up)("n-checkbox"),f=(0,n.up)("n-space"),p=(0,n.up)("n-checkbox-group"),m=(0,n.up)("n-radio"),g=(0,n.up)("n-radio-group"),w=(0,n.up)("n-form-item"),v=(0,n.up)("n-gi"),h=(0,n.up)("n-button"),b=(0,n.up)("DownOutlined"),y=(0,n.up)("UpOutlined"),k=(0,n.up)("n-grid"),S=(0,n.up)("n-form");return(0,n.wg)(),(0,n.j4)(S,(0,n.dG)(e.getBindValue,{model:e.formModel,ref:"formElRef"}),{default:(0,n.w5)((()=>[(0,n.Wm)(k,(0,o.vs)((0,n.F4)(e.getGrid)),{default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(e.getSchema,(t=>((0,n.wg)(),(0,n.j4)(v,(0,n.dG)(t.giProps,{key:t.field}),{default:(0,n.w5)((()=>[(0,n.Wm)(w,{label:t.label,path:t.field},(0,n.Nv)({default:(0,n.w5)((()=>[t.slot?(0,n.WI)(e.$slots,t.slot,{key:0,model:e.formModel,field:t.field,value:e.formModel[t.field]},void 0,!0):"NCheckbox"===t.component?((0,n.wg)(),(0,n.j4)(p,{key:1,value:e.formModel[t.field],"onUpdate:value":l=>e.formModel[t.field]=l},{default:(0,n.w5)((()=>[(0,n.Wm)(f,null,{default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(t.componentProps.options,(e=>((0,n.wg)(),(0,n.j4)(c,{key:e.value,value:e.value,label:e.value},null,8,["value","label"])))),128))])),_:2},1024)])),_:2},1032,["value","onUpdate:value"])):"NRadioGroup"===t.component?((0,n.wg)(),(0,n.j4)(g,{key:2,value:e.formModel[t.field],"onUpdate:value":l=>e.formModel[t.field]=l},{default:(0,n.w5)((()=>[(0,n.Wm)(f,null,{default:(0,n.w5)((()=>[((0,n.wg)(!0),(0,n.iD)(n.HY,null,(0,n.Ko)(t.componentProps.options,(e=>((0,n.wg)(),(0,n.j4)(m,{key:e.value,value:e.value},{default:(0,n.w5)((()=>[(0,n.Uk)((0,o.zw)(e.label),1)])),_:2},1032,["value"])))),128))])),_:2},1024)])),_:2},1032,["value","onUpdate:value"])):((0,n.wg)(),(0,n.j4)((0,n.LL)(t.component),(0,n.dG)({key:3},e.getComponentProps(t),{value:e.formModel[t.field],"onUpdate:value":l=>e.formModel[t.field]=l,class:{isFull:0!=t.isFull&&e.getProps.isFull}}),null,16,["value","onUpdate:value","class"])),t.suffix?(0,n.WI)(e.$slots,e.schem.suffix,{key:4,model:e.formModel,field:e.schem.field,value:e.formModel[e.schem.field]},void 0,!0):(0,n.kq)("",!0)])),_:2},[t.labelMessage?{name:"label",fn:(0,n.w5)((()=>[(0,n.Uk)((0,o.zw)(t.label)+" ",1),(0,n.Wm)(d,{trigger:"hover",style:(0,o.j5)(t.labelMessageStyle)},{trigger:(0,n.w5)((()=>[(0,n.Wm)(r,{size:"18",class:"cursor-pointer text-gray-400"},{default:(0,n.w5)((()=>[(0,n.Wm)(s)])),_:1})])),default:(0,n.w5)((()=>[(0,n.Uk)(" "+(0,o.zw)(t.labelMessage),1)])),_:2},1032,["style"])])),key:"0"}:void 0]),1032,["label","path"])])),_:2},1040)))),128)),e.getProps.showActionButtonGroup?((0,n.wg)(),(0,n.j4)(v,{key:0,span:e.isInline?"":24,suffix:!!e.isInline},{default:(0,n.w5)((({overflow:t})=>[(0,n.Wm)(f,{align:"center",justify:e.isInline?"end":"start",style:(0,o.j5)({"margin-left":`${e.isInline?12:e.getProps.labelWidth}px`})},{default:(0,n.w5)((()=>[e.getProps.showSubmitButton?((0,n.wg)(),(0,n.j4)(h,(0,n.dG)({key:0},e.getSubmitBtnOptions,{onClick:e.handleSubmit,loading:e.loadingSub,secondary:""}),{default:(0,n.w5)((()=>[(0,n.Uk)((0,o.zw)(e.getProps.submitButtonText),1)])),_:1},16,["onClick","loading"])):(0,n.kq)("",!0),e.getProps.showResetButton?((0,n.wg)(),(0,n.j4)(h,(0,n.dG)({key:1},e.getResetBtnOptions,{onClick:e.resetFields}),{default:(0,n.w5)((()=>[(0,n.Uk)((0,o.zw)(e.getProps.resetButtonText),1)])),_:1},16,["onClick"])):(0,n.kq)("",!0),e.isInline&&e.getProps.showAdvancedButton?((0,n.wg)(),(0,n.j4)(h,{key:2,type:"primary",text:"","icon-placement":"right",onClick:e.unfoldToggle},{icon:(0,n.w5)((()=>[t?((0,n.wg)(),(0,n.j4)(r,{key:0,size:"14",class:"unfold-icon"},{default:(0,n.w5)((()=>[(0,n.Wm)(b)])),_:1})):((0,n.wg)(),(0,n.j4)(r,{key:1,size:"14",class:"unfold-icon"},{default:(0,n.w5)((()=>[(0,n.Wm)(y)])),_:1}))])),default:(0,n.w5)((()=>[(0,n.Uk)(" "+(0,o.zw)(t?"展开":"收起"),1)])),_:2},1032,["onClick"])):(0,n.kq)("",!0)])),_:2},1032,["justify","style"])])),_:1},8,["span","suffix"])):(0,n.kq)("",!0)])),_:3},16)])),_:3},16,["model"])}var i=l(57933),u=l(44870);const s={labelWidth:{type:[Number,String],default:80},schemas:{type:[Array],default:()=>[]},layout:{type:String,default:"inline"},inline:{type:Boolean,default:!1},size:{type:String,default:"medium"},labelPlacement:{type:String,default:"left"},isFull:{type:Boolean,default:!0},showActionButtonGroup:{default:!0},showResetButton:{default:!0},resetButtonOptions:Object,showSubmitButton:{default:!0},submitButtonOptions:Object,showAdvancedButton:{default:!0},submitButtonText:{type:String,default:"查询"},resetButtonText:{type:String,default:"重置"},gridProps:Object,giProps:Object,baseGridStyle:{type:Object},collapsed:{type:Boolean,default:!1},collapsedRows:{type:Number,default:1}};var r=l(97109);const d={xmlns:"http://www.w3.org/2000/svg","xmlns:xlink":"http://www.w3.org/1999/xlink",viewBox:"0 0 1024 1024"},c=(0,n._)("path",{d:"M890.5 755.3L537.9 269.2c-12.8-17.6-39-17.6-51.7 0L133.5 755.3A8 8 0 0 0 140 768h75c5.1 0 9.9-2.5 12.9-6.6L512 369.8l284.1 391.6c3 4.1 7.8 6.6 12.9 6.6h75c6.5 0 10.3-7.4 6.5-12.7z",fill:"currentColor"},null,-1),f=[c];var p=(0,n.aZ)({name:"UpOutlined",render:function(e,t){return(0,n.wg)(),(0,n.iD)("svg",d,f)}}),m=l(29386);function g(e){return"NInput"===e?"请输入":["NPicker","NSelect","NCheckbox","NRadio","NSwitch","NDatePicker","NTimePicker"].includes(e)?"请选择":""}const w=["DatePicker","MonthPicker","WeekPicker","TimePicker"];function v(){return[...w,"RangePicker"]}v();var h=l(80809);function b({emit:e,getProps:t,formModel:l,getSchema:n,formElRef:o,defaultFormModel:a,loadingSub:i,handleFormValues:s}){async function r(){return(0,u.SU)(o)?.validate()}async function d(n){n&&n.preventDefault(),i.value=!0;const{submitFunc:a}=(0,u.SU)(t);if(a&&(0,h.mf)(a))return void await a();const s=(0,u.SU)(o);if(s)try{return await r(),i.value=!1,void e("submit",l)}catch(d){return void(i.value=!1)}}async function c(){await((0,u.SU)(o)?.restoreValidation())}async function f(){const{resetFunc:n,submitOnReset:i}=(0,u.SU)(t);f&&(0,h.mf)(n)&&await n();const r=(0,u.SU)(o);if(!r)return;Object.keys(l).forEach((e=>{l[e]=(0,u.SU)(a)[e]||null})),await c();const p=s((0,u.IU)((0,u.SU)(l)));e("reset",p),i&&await d()}function p(){const e=(0,u.SU)(o);if(e)return s((0,u.IU)(l))}async function m(e){const t=(0,u.SU)(n).map((e=>e.field)).filter(Boolean);Object.keys(e).forEach((n=>{const o=e[n];t.includes(n)&&(l[n]=o)}))}return{handleSubmit:d,validate:r,resetFields:f,getFieldsValue:p,clearValidate:c,setFieldsValue:m}}var y=l(39439);function k({defaultFormModel:e,getSchema:t,formModel:l}){function n(e){if(!(0,h.Kn)(e))return{};const t={};for(const l of Object.entries(e)){let[,e]=l;const[n]=l;!n||(0,h.kJ)(e)&&0===e.length||(0,h.mf)(e)||(0,h.BD)(e)||((0,h.HD)(e)&&(e=e.trim()),(0,y.Z)(t,n,e))}return t}function o(){const n=(0,u.SU)(t),o={};n.forEach((e=>{const{defaultValue:t}=e;(0,h.BD)(t)||(o[e.field]=t,l[e.field]=t)})),e.value=o}return{handleFormValues:n,initDefault:o}}var S=l(42325),U=(0,n.aZ)({name:"BasicUpload",components:{DownOutlined:r.Z,UpOutlined:p,QuestionCircleOutlined:m.Z},props:{...s},emits:["reset","submit","register"],setup(e,{emit:t,attrs:l}){const o=(0,u.iH)({}),a=(0,u.qj)({}),s=(0,u.iH)({}),r=(0,u.iH)(null),d=(0,u.iH)(null),c=(0,u.iH)(!0),f=(0,u.iH)(!1),p=(0,u.iH)(!1),m=(0,n.Fl)((()=>Object.assign({size:e.size,type:"primary"},e.submitButtonOptions))),w=(0,n.Fl)((()=>Object.assign({size:e.size,type:"default"},e.resetButtonOptions)));function v(e){const t=e.componentProps??{},l=e.component;return{clearable:!0,placeholder:g((0,u.SU)(l)),...t}}const h=(0,n.Fl)((()=>{const t={...e,...(0,u.SU)(s)},l={rules:{}},n=t.schemas||[];return n.forEach((e=>{e.rules&&(0,i.Z)(e.rules)&&(l.rules[e.field]=e.rules)})),{...t,...(0,u.SU)(l)}})),y=(0,n.Fl)((()=>{const{layout:e}=(0,u.SU)(h);return"inline"===e})),U=(0,n.Fl)((()=>{const{gridProps:e}=(0,u.SU)(h);return{...e,collapsed:!!y.value&&c.value,responsive:"screen"}})),F=(0,n.Fl)((()=>({...l,...e,...(0,u.SU)(h)}))),P=(0,n.Fl)((()=>{const e=(0,u.SU)(r)||h.value.schemas;for(const t of e){const{defaultValue:e}=t;e&&(t.defaultValue=e)}return e})),{handleFormValues:B,initDefault:j}=k({defaultFormModel:o,getSchema:P,formModel:a}),{handleSubmit:O,validate:M,resetFields:V,getFieldsValue:_,clearValidate:x,setFieldsValue:z}=b({emit:t,getProps:h,formModel:a,getSchema:P,formElRef:d,defaultFormModel:o,loadingSub:f,handleFormValues:B});function C(){c.value=!c.value}async function W(e){s.value=(0,S.RH)((0,u.SU)(s)||{},e)}const D={getFieldsValue:_,setFieldsValue:z,resetFields:V,validate:M,clearValidate:x,setProps:W,submit:O};return(0,n.YP)((()=>P.value),(e=>{(0,u.SU)(p)||e?.length&&(j(),p.value=!0)})),(0,n.bv)((()=>{j(),t("register",D)})),{formElRef:d,formModel:a,getGrid:U,getProps:h,getBindValue:F,getSchema:P,getSubmitBtnOptions:m,getResetBtnOptions:w,handleSubmit:O,resetFields:V,loadingSub:f,isInline:y,getComponentProps:v,unfoldToggle:C}}}),F=l(40089);const P=(0,F.Z)(U,[["render",a],["__scopeId","data-v-fef3b708"]]);var B=P,j=l(69896);function O(e){const t=(0,u.iH)(null),l=(0,u.iH)(!1);async function o(){const e=(0,u.SU)(t);return e||console.error("The form instance has not been obtained, please make sure that the form has been rendered when performing the form operation!"),await(0,n.Y3)(),e}function a(o){(0,j.Br)()&&(0,n.Ah)((()=>{t.value=null,l.value=null})),(0,u.SU)(l)&&(0,j.Br)()&&o===(0,u.SU)(t)||(t.value=o,l.value=!0,(0,n.YP)((()=>e),(()=>{e&&o.setProps((0,S.tk)(e))}),{immediate:!0,deep:!0}))}const i={setProps:async e=>{const t=await o();await t.setProps(e)},resetFields:async()=>{o().then((async e=>{await e.resetFields()}))},clearValidate:async e=>{const t=await o();await t.clearValidate(e)},getFieldsValue:()=>(0,u.SU)(t)?.getFieldsValue(),setFieldValue:async e=>{const t=await o();return t.setFieldValue(e)},submit:async()=>{const e=await o();return e.submit()},validate:async e=>{const t=await o();return t.validate(e)}};return[a,i]}}}]);
//# sourceMappingURL=684.d3d9db03.js.map