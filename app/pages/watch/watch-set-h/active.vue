<!-- 活动目标 -->
<template>
  <app-body :bg="false" :bodyStyle="{backgroundColor:'#FFF'}" title="活动目标">
    <!-- <app-logo text="活动目标"></app-logo> -->
    <view class="ui-cell">
      <u-cell-group>
        <u-cell :title="item.name" v-for="(item,index) in cellList" :key="index" arrow-direction="right"
          titleStyle="font-size: 15px;color: #303133;">
          <view slot="value" class="u-slot-value">
            <u-number-box v-model="item.value" :step='item.step' :min="item.min" :max="item.max"
              :showMinus="item.value > item.min">
              <view slot="minus" class="minus">
                <u-icon name="minus" size="12"></u-icon>
              </view>
              <input slot="input" style="width: 50px;text-align: center;" class="input" v-model="item.value"
                type="number" @blur="blurInput(item)"></input>
              <view slot="plus" class="plus">
                <u-icon name="plus" color="#FFFFFF" size="12"></u-icon>
              </view>
            </u-number-box>
          </view>
        </u-cell>
      </u-cell-group>
    </view>
    <view class="ui-confirm">
      <button class="ui-btn" @click="cancel">返回</button>
      <button class="default" @click="submit">确认</button>
    </view>
  </app-body>
</template>

<script>
  import {
    mapState,
  } from 'vuex';
  import {
    PostCreateExerciseTarget,
    GetExerciseTarget
  } from '@/common/http/api';
  export default {
    data() {
      return {
        cellList: [{
            name: '卡路里',
            min: 200,
            max: 1000,
            step: 100,
            value: 0
          },
          {
            name: '步数',
            min: 3000,
            max: 40000,
            step: 1000,
            value: 0
          },
          {
            name: '体重',
            min: 25,
            max: 200,
            step: 5,
            value: 0
          }
        ],
        exerciseTargetId: 0
      }
    },
    computed: {
      ...mapState({
        deviceInfo: state => state.deviceInfo
      }, )
    },
    mounted() {

    },
    methods: {
      handleSelect() {

      },
      handleInit() {
        GetExerciseTarget({
          deviceId: this.deviceInfo.deviceId,
          humanId: this.deviceInfo.humanId
        }).then(res => {
          console.log(res, 'res')
          this.cellList[0].value = res.data.calorie
          this.cellList[1].value = res.data.stepNum
          this.cellList[2].value = res.data.weight
          this.exerciseTargetId = res.data.exerciseTargetId
        })
      },
      cancel() {
        uni.navigateBack()
      },
      submit() {
        PostCreateExerciseTarget({
          deviceId: this.deviceInfo.deviceId,
          calorie: this.cellList[0].value,
          stepNum: this.cellList[1].value,
          weight: this.cellList[2].value,
          exerciseDuration: '',
          exerciseTargetId: this.exerciseTargetId || null
        }).then(res => {
          console.log(res, 'res')
          uni.$u.toast('操作成功')
          setTimeout(() => {
            this.handleInit()
          }, 1000)
        })
      },
      blurInput(item) {
        if (item.value > item.max) {
          item.value = item.max
        } else if (item.value < item.min) {
          item.value = item.min
        }
        if (item.value % 1 !== 0) {
          item.value = Math.ceil(item.value)
        }
      }
    },
    onShow() {
      this.handleInit()
    }
  }
</script>

<style lang="scss" scoped>
  .ui-cell {
    margin-top: 64rpx;
  }

  .minus {
    width: 22px;
    height: 22px;
    border-width: 1px;
    border-color: #E6E6E6;
    border-style: solid;
    border-top-left-radius: 100px;
    border-top-right-radius: 100px;
    border-bottom-left-radius: 100px;
    border-bottom-right-radius: 100px;
    @include flex;
    justify-content: center;
    align-items: center;
  }

  .input {
    padding: 0 10px;
  }

  .plus {
    width: 22px;
    height: 22px;
    background-color: #fd963e;
    border-radius: 50%;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    justify-content: center;
    align-items: center;
  }

  .ui-confirm {
    width: 100%;
    display: flex;
    margin-top: 104rpx;

    button {
      width: 320rpx;
    }
  }

  .ui-btn {
    background-color: #FFF !important;
    color: #fd963e;
    border: 1px solid #fd963e;
  }
</style>
