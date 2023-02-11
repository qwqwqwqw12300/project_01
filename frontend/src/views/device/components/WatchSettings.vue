<template>
  <form-panel title="监护设备设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs v-model="currentTab">
          <el-tab-pane label="设备定位" name="first" >
            <el-row type="flex" justify="center">
                <span v-show="reLocating">获取机器定位中...</span>
                <span v-show="!reLocating">定位完毕</span>
            </el-row>
            <el-row type="flex" justify="center">
                <gao-de-map ref="gaoDeMap" style="height: 400px;width: 400px" v-model="location" :address.sync="address" :reLocating.sync="reLocating"></gao-de-map>
              </el-row>
            <el-row type="flex" justify="center">
              <el-card :body-style="{width:'400px'}">当前地址：{{address}}</el-card>
            </el-row>
            <el-row type="flex" justify="center">
              <el-button type="primary" @click="()=>this.$refs['gaoDeMap'].reLocateCurrentPosition()">立即获取定位</el-button>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="设置电子围栏" name="second">
            <el-row type="flex" justify="center">
              <gao-de-map v-if="currentTab == 'second'" v-model="location" show-circle="true" :circle-radius="radius" style="height: 400px;width: 400px" ></gao-de-map>
            </el-row>
            <el-row type="flex" justify="center">
              <el-card :body-style="{width:'400px'}">
                <el-row type="flex" justify="center">
                  <el-input v-model="address" placeholder="请输入地址"></el-input>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col :span="20"><span>设置半径长度</span></el-col>
                  <el-col :span="4"><span>200米</span></el-col>
                </el-row>
                <el-row type="flex" justify="center">
                  <el-col>
                    <el-slider v-model="radius" style="width:90%" :min="1" :max="200"></el-slider>
                  </el-col>
                </el-row>
              </el-card>
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="设置联系电话" name="third">
                  
        <!-- <p3>最终提交参数：{{this.settings}}</p3> -->
        <br>
              <el-row type="flex" justify="center" style="margin-top: 20px">
                      <el-button type="primary" @click="handleAdd"  icon="el-icon-plus">添加联系人</el-button>
                      <!-- <el-button type="primary" @click="handleAdd"  icon="el-icon-plus" :disabled="this.settings.list.length >=4">添加联系人</el-button> -->
              </el-row>

            <el-row type="flex" class="row-bg" justify="center">

                  <el-col :span="12" class="left-box">
                    <el-row type="flex" justify="center">
                      <water-fall :value="settings.list"  :column-number="1">
                        <template slot-scope="{item,rowIndex}">
                          <el-card class="box-card" shadow='always' :body-style="{width:'100%'}"  style="margin-bottom: 2px"  @click.native="selectPhone(rowIndex)">
                            <div slot="header" class="clearfix" >
                                <i class="el-icon-user-solid"></i>{{item.phoneName}}
                                <el-button style="float: right; padding: 6px 7px" icon="el-icon-close" type="danger" @click="removePhone(rowIndex)" @click.stop.native >删除</el-button>
                                <el-button style="float: right; padding: 6px 7px" icon="el-icon-s-tools" type="primary" @click="updatePhone(rowIndex)">设置</el-button>
                                <!-- @click="removeZones(rowIndex)" @click.stop.native -->

                            </div>
                            <el-row>
                                <span>{{item.phone}}</span>
                            </el-row>                      
                          </el-card>
                        </template>
                      </water-fall>
                    </el-row>
                  </el-col> 
                  <el-col :span="12"  class="right-box">
                          <el-row  justify="center" type="flex"   v-for="(item,key) in this.settings.mapSet">
                              <el-card class="box-card" shadow='always' :body-style="{width:'100%'}"  style="margin-bottom: 2px" >
                                    <div slot="header" class="clearfix" >
                                      <i class="el-icon-phone"></i>{{item.phoneName}}(按键:{{typeShow[key]}})
                                      <el-button style="float: right; padding: 6px 7px" icon="el-icon-close" type="danger" @click="removeSosPhone(key)" @click.stop.native >删除</el-button>   
                                      <el-button style="float: right; padding: 6px 7px" icon="el-icon-s-tools" type="primary" @click="updateSosPhone(key)">设置</el-button>
      
                                </div>
                                  <el-row>
                                  <span>{{item.phone}}</span>
                                  </el-row>
                                </el-card>
                        </el-row>
                  </el-col>                
                </el-row>   
          
        </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary"  @click="submitForm">提 交</el-button>
      <el-button @click="open = false">取 消</el-button>

    </div>
    <!-- 设置联系电话弹窗 -->
    <el-dialog :title="title" :visible.sync="setPhone" width="500px" append-to-body>  
      <el-form ref="addPhoneSettingsForm" :rules="settingsPhoneRules"  :inline="true" label-width="125px"  >
          <el-row>        
            <el-col type="flex" justify="center">
              <el-form-item label="姓名" prop="phoneName">
                <el-input v-model="addPhoneform.phoneName"  placeholder="请输入姓名" maxlength="16" />
              </el-form-item>
            </el-col>          
          </el-row>
          <el-row type="flex" justify="center" >
            <el-col>
              <el-form-item label="手机号码" prop="phone">
                <el-input v-model="addPhoneform.phone" placeholder="请输入手机号码" maxlength="11" />
              </el-form-item>
            </el-col>          
          </el-row>   
          <el-row  justify="center" type="flex">
              <el-radio-group v-model="addPhoneform.phoneType" size="medium">
                <el-row   v-for="(item, index) in setTypeListOptions">
                  <el-col :span="10">
                      <el-radio
                        :key="index"
                        :label="item.value"
                        :disabled="item.disabled"
                        >{{ item.label }}
                      </el-radio>
                     </el-col>
                  <!-- </div>  -->
              </el-row>
               </el-radio-group>
         </el-row>
      </el-form> 


      <div slot="footer" class="dialog-footer">
        <el-button type="primary"  @click="addPhone">确 定</el-button>
        <el-button @click="setPhone = false">取 消</el-button>
      </div>
    </el-dialog>

  </form-panel>
</template>

<script>
export default {
  name: "WatchSettings",
  props: {
    // settings: {
    //   type: Object,
    //   default(){
    //     return {
    //       type:"1"
    //     }
    //   }
    // },
    visible: {
      type: Boolean,
      default: false
    },
    value: {
      type: Object,
      default(){
        return {
          type:"1",
          settingsDeviceId:undefined,    
          list: [],
				  mapSet: {},              
        }
      }
    }
  },

  watch:{
    "visible":{
      immediate: true,
      handler: function (val) {
        if(val == true){
          // if(this.settings.type == undefined){
          //   this.settings.type = "1"
          // }
          
        
          if(this.settings.mapSet == undefined){
            this.settings.mapSet = {}
          }
          if(this.settings.list == undefined){
            this.settings.list = []
          }
          // if(this.settingsDeviceId == undefined){
          //   this.settings.settingsDeviceId = []
          // }
          this.$emit('input', this.settings)
          // this.init = true
        }
      }
    }
  },

  data(){
    return {
      currentTab:'third',
      reLocating:false,
      address:"",
      location:undefined,
      radius:100,
      title:"",
      // 是否显示弹出层
      setPhone:false,
      // 表单参数
      addPhoneform: {
        phoneName:undefined,
        phone:undefined,
        phoneType:'P',

      },

      setTypeListOptions:[
        { 
            value:0,
            label:'设置为SOS(默认)'
        },
        { 
            value:1,
            label:'设置为按钮1'
        },
        { 
            value:2,
            label:'设置为按钮2'
        },
        { 
            value:3,
            label:'设置为按钮3'
        },
        { 
            value:'P',
            label:'暂保存，不设置'
        },


      ],

      typeShow: {
					0: 'SOS',
					1: '1',
					2: '2',
					3: '3',
					P: 'P',
				}
        ,
        
      dynamicPhoneForm: {
          phones: [{
            value: ''
          }],
          name:"SOS"
        },
        //联系人表单校验
        settingsPhoneRules:{
            phoneName:[
              { required: true, message: "姓名不能为空", trigger: "blur" },
              { max: 20, message: '姓名长度不能超过 20 字', trigger: 'blur' }   
            ],
            phone:[
              { required: true, message: "手机号码不能为空", trigger: "blur" },
                {
                  pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
                  message: "请输入正确的手机号码",
                  trigger: "blur"
                }
            ],
            phoneType:[
              { required: true, message: "必须选择设置类型", trigger: "blur" },
            ],
            
        },

    }  
  },
  computed: {
    open: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },

    settings:{
      get(){
        return this.value
      },
      set(val){
        this.$emit('input', val)
      }
    },

    lists:{
      get(){
        return this.index == undefined?undefined:this.settings.list[this.index]
      },
      set(val){
        if(this.index != undefined&&this.settings.list[this.index]!=undefined){
          this.settings.list.splice(this.index,1,val);
        }
      }
    },
    

  },
  methods: {
    
    async submitForm() {
                  this.$emit('submit', this.settings)

      // try{
      //   const valid = await this.$refs["WatchSettingsForm"].validate();
      //   if(valid){
      //     if(this.settings?.list?.length>0){
      //       let currentIndex = this.index;
      //       for(let i = 0;i<this.settings?.list?.length;i++){
      //         this.index = i;
      //         await this.$refs["phoneSettingsForm"]?.clearValidate();
      //         try{
      //           const result =await this.$refs["phoneSettingsForm"].validate();
      //           if(result == false){
      //             this.currentTab = 'third'
      //             return
      //           }
      //         }catch (e){
      //           this.currentTab = 'third'
      //           return
      //         }
      //       }
      //       this.index = currentIndex
      //       this.$emit('submit', this.settings)
      //     }else{
      //       this.$emit('submit', this.settings)
      //     }
      //   }else{
      //     this.currentTab = 'first'
      //   }
      // }catch (e){
      //   this.currentTab = 'first'
      // }

    },
    handleAdd()
    {
      
      this.setPhone=true;
      this.title="新增联系人";
    }
    ,
    updatePhone(rowIndex){
      if(this.addPhoneform.phoneType=="P"){

      }
      this.addPhoneform.phoneName = this.settings.list[rowIndex].phoneName,
      this.addPhoneform.phone = this.settings.list[rowIndex].phone,
      this.addPhoneform.phoneType ="P"

      this.setPhone=true;
      this.title="修改联系人";
    },
    updateSosPhone(key){

      const obj={phoneName:this.addPhoneform.phoneName,phone:this.addPhoneform.phone};
              // this.settings.mapSet.set[this.addPhoneform.phoneType]=obj;
              // this.settings.mapSet.set("1","2");
              this.settings.mapSet[this.addPhoneform.phoneType] = obj

      this.setPhone=true;
      this.title="修改联系人";        
    },
    async addPhone(){
      // const valid = await this.$refs["addPhoneSettingsForm"].validate();
      //   if(valid){
            if(this.addPhoneform.phoneType=="P"){
              for (let [k, v] of Object.entries(this.settings.mapSet)) {
                if (v.phone === this.addPhoneform.phone){
                  this.removeSosPhone(k)
                }
              }
          
              this.settings.list.push({
                phoneName:this.addPhoneform.phoneName,  
                phone:this.addPhoneform.phone
              });
    
            }else{
              const obj={phoneName:this.addPhoneform.phoneName,phone:this.addPhoneform.phone};
              // this.settings.mapSet.set[this.addPhoneform.phoneType]=obj;
              // this.settings.mapSet.set("1","2");
              const index = this.settings.list.findIndex(i => i.phone === obj.phone)
              if (index != -1) this.removePhone(index)
              this.settings.mapSet[this.addPhoneform.phoneType] = obj
              
            }
            this.index = this.settings.list.length-1
            this.setPhone=false;
          // }

    },

    removePhone(index){
      this.settings.list.splice(index,1);
    },

    removeSosPhone(key){
      // delete this.settings.mapSet[key]; // 无法实时更新，原生delete并不能被vue检测到变化
      this.$delete(this.settings.mapSet, key)
    },

    async selectPhone(index){
      this.index = index;
      this.$refs["phoneSettingsForm"]?.clearValidate();
      this.$refs["phoneSettingsForm"]?.validate();
    },

  }
}
</script>
<!-- 样式部分 -->
<style lang="scss" scoped>
  /* .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  } */

   .box-card {
    width: 450px;
  } 
  .el-row {
  margin-bottom: 20px;

  &
  :last-child {
    margin-bottom: 0;
  }
  .right-box,.left-box{
    border-radius: 10px;
    padding: 20px;
    // max-height: 500px;
    // overflow-y: scroll;

  }
  .right-box{
    background-color:darkgray;
    
    margin-left: 10px;

  }

  .left-box{
    background-color:dimgrey;
    margin-right: 10px;

  }

}
</style>
