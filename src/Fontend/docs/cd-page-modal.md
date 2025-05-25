
# cd-page-modal 页面级弹窗组件

## 组件概述

`cd-page-modal` 一个页面级模态弹窗组件，支持队列管理、去重机制、富文本渲染、表单输入等特性。适用于 H5 和 App 平台，能够满足各种弹窗交互场景。

### 主要特性

- **弹窗队列管理**：同一时间只显示一个弹窗，其余弹窗排队等待
- **弹窗去重**：通过 key 标识去重，避免重复弹窗
- **样式自定义**：支持自定义标题、内容、按钮样式
- **富文本渲染**：支持 HTML 内容和图片展示
- **表单功能**：支持单行和多行输入，带验证功能
- **异步操作**：支持异步加载和操作
- **静默管理**：支持静默删除特定弹窗或清空全部
- **动态更新**：支持实时更新弹窗内容和配置
- **增强遮罩**：支持背景模糊和全屏遮罩效果

## 基础使用

### 引入组件

```vue
<cd-page-modal></cd-page-modal>
```

### 显示基础弹窗

```javascript
uni.$emit('cd-page-modal:show', {
  title: '提示',
  content: '这是一个基础弹窗'
}, () => {
  // 确认回调
}, () => {
  // 取消回调
});
```

## API 文档

### 事件

| 事件名 | 说明 | 参数 |
|-------|------|------|
| cd-page-modal:show | 显示弹窗 | config, confirmCallback, cancelCallback, key |
| cd-page-modal:close | 关闭当前弹窗 | - |
| cd-page-modal:close-by-id | 关闭指定ID的弹窗 | id, skipCallback |
| cd-page-modal:clear-all | 清空所有弹窗 | - |
| cd-page-modal:finish-async | 完成异步操作并关闭 | - |

### 配置选项

#### 基础配置

| 参数 | 类型 | 默认值 | 说明 |
|-----|------|-------|------|
| title | String | '提示' | 弹窗标题 |
| content | String | '' | 弹窗内容文本 |
| showTitle | Boolean | true | 是否显示标题 |
| showConfirmButton | Boolean | true | 是否显示确认按钮 |
| showCancelButton | Boolean | false | 是否显示取消按钮 |
| confirmText | String | '确定' | 确认按钮文本 |
| cancelText | String | '取消' | 取消按钮文本 |
| confirmColor | String | '#2979ff' | 确认按钮颜色 |
| cancelColor | String | '#606266' | 取消按钮颜色 |
| width | Number | 600 | 弹窗宽度(rpx) |
| borderRadius | Number | 12 | 弹窗圆角大小(rpx) |
| asyncClose | Boolean | false | 是否异步关闭 |
| maskCloseAble | Boolean | false | 点击遮罩是否可关闭 |
| customClass | String | '' | 自定义类名 |

#### 样式配置

| 参数 | 类型 | 默认值 | 说明 |
|-----|------|-------|------|
| titleStyle | Object | {} | 标题样式 |
| contentStyle | Object | {} | 内容样式 |
| cancelStyle | Object | {} | 取消按钮样式 |
| confirmStyle | Object | {} | 确认按钮样式 |
| negativeTop | Number | 0 | 弹窗向上偏移量(rpx) |

#### 遮罩层配置

| 参数 | 类型 | 默认值 | 说明 |
|-----|------|-------|------|
| overlay | Boolean | true | 是否显示遮罩层 |
| overlayColor | String | 'rgba(0, 0, 0, 0.5)' | 遮罩层颜色 |
| overlayOpacity | Number | 0.5 | 遮罩层不透明度(0-1) |
| overlayBlur | Number | 0 | 遮罩层背景模糊效果(px) |
| overlayClass | String | '' | 遮罩层自定义类名 |
| maskFullScreen | Boolean | false | 遮罩层是否全屏显示 |

#### 输入框配置

| 参数 | 类型 | 默认值 | 说明 |
|-----|------|-------|------|
| showInput | Boolean | false | 是否显示输入框 |
| inputType | String | 'text' | 输入框类型(text/textarea/password等) |
| inputValue | String | '' | 输入框初始值 |
| inputPlaceholder | String | '请输入' | 输入框占位文本 |
| inputMaxlength | Number | 100 | 最大输入长度 |
| inputRows | Number | 4 | 多行文本时的行数 |
| inputValidator | Function | null | 输入验证函数 |
| inputErrorMessage | String | '' | 输入错误提示 |

#### 富文本与其他配置

| 参数 | 类型 | 默认值 | 说明 |
|-----|------|-------|------|
| useSlot | Boolean | false | 是否使用插槽 |
| richContent | String | '' | 富文本内容(HTML) |
| zIndex | Number | 1000 | 弹窗层级 |
| closeOnPressEscape | Boolean | false | 按ESC键是否关闭 |
| appendToBody | Boolean | true | 是否挂载到body |
| fullscreen | Boolean | false | 是否全屏显示 |
| lockScroll | Boolean | true | 是否锁定滚动 |
| animationDuration | Number | 300 | 动画持续时间(ms) |
| animationType | String | 'fade' | 动画类型(fade/scale/slide-top/slide-bottom) |
| showCloseIcon | Boolean | false | 是否显示关闭图标 |
| closeIconPosition | String | 'top-right' | 关闭图标位置(top-right/top-left/in-header) |

### 方法

通过 ref 获取组件实例后，可以调用以下方法：

| 方法名 | 说明 | 参数 |
|-------|------|------|
| showModal | 显示弹窗 | config, confirmCallback, cancelCallback, key |
| closeModal | 关闭当前弹窗 | - |
| closeModalById | 关闭指定ID的弹窗 | id, skipCallback |
| clearLoading | 清除加载状态 | - |
| finishAsync | 完成异步操作并关闭 | - |
| clearAllModals | 清空所有弹窗 | - |

## 使用示例

### 基础弹窗

```javascript
uni.$emit('cd-page-modal:show', {
  title: '基础弹窗',
  content: '这是一个基础弹窗示例',
  showCancelButton: true
}, () => {
  uni.showToast({ title: '点击了确认' });
}, () => {
  uni.showToast({ title: '点击了取消' });
});
```

### 带输入框的弹窗

```javascript
uni.$emit('cd-page-modal:show', {
  title: '输入框示例',
  content: '请在下方输入内容',
  showInput: true,
  inputType: 'text',
  inputPlaceholder: '请输入内容',
  inputValidator: (value) => value.length >= 2,
  inputErrorMessage: '至少输入2个字符',
  showCancelButton: true
}, (value) => {
  uni.showToast({ title: `您输入了: ${value}` });
});
```

### 富文本弹窗

```javascript
uni.$emit('cd-page-modal:show', {
  title: '富文本示例',
  richContent: `
    <div style="padding: 15px; color: #2979ff;">
      这是一段<strong>富文本</strong>内容
      <img src="/static/logo.png" style="width: 100px; height: 100px;" />
    </div>
  `,
  width: 650
});
```

### 异步关闭弹窗

```javascript
uni.$emit('cd-page-modal:show', {
  title: '异步操作',
  content: '加载中...',
  asyncClose: true,
  showCancelButton: false
}, () => {
  // 模拟异步操作
  setTimeout(() => {
    // 完成操作并关闭弹窗
    uni.$emit('cd-page-modal:finish-async');
  }, 3000);
});
```

### 增强遮罩效果

```javascript
uni.$emit('cd-page-modal:show', {
  title: '增强遮罩效果',
  content: '这个弹窗有模糊背景效果',
  overlayOpacity: 0.8,
  overlayBlur: 5,
  maskFullScreen: true,
  showCloseIcon: true
});
```

### 弹窗去重示例

```javascript
// 使用相同的key，不会重复添加
const loginKey = 'login_required';

// 多次调用，但队列中只会有一个
uni.$emit('cd-page-modal:show', {
  title: '登录提示',
  content: '您需要先登录才能继续操作'
}, () => {
  uni.navigateTo({ url: '/pages/login/login' });
}, null, loginKey);
```

### 动态更新弹窗

```javascript
// 先添加一个弹窗
const modalId = uni.$emit('cd-page-modal:show', {
  title: '初始弹窗',
  content: '内容将在3秒后更新',
  asyncClose: true
});

// 3秒后更新内容
setTimeout(() => {
  // 导入 updateModal 函数
  import { updateModal } from '../../stores/modules/modal.js';
  
  // 更新弹窗内容
  updateModal(modalId, {
    title: '已更新',
    content: '弹窗内容已更新',
    confirmColor: '#19be6b'
  });
}, 3000);
```

## 高级用法

### 配置队列排序规则

```javascript
// 导入 reorderModalQueue 函数
import { reorderModalQueue } from '../../stores/modules/modal.js';

// 设置为先进先出顺序
reorderModalQueue('fifo');

// 设置为后进先出顺序(默认)
reorderModalQueue('lifo');
```

### 静默删除特定弹窗

```javascript
// 导入 closeModalById 函数
import { closeModalById } from '../../stores/modules/modal.js';

// 关闭特定ID的弹窗，不触发回调
closeModalById('modal_id', true);
```

## 常见问题

1. **弹窗不显示或样式异常**
   - 检查配置参数是否正确
   - 确认组件是否正确引入
   - 查看控制台是否有错误信息

2. **富文本内容不正确**
   - 确保HTML标签闭合正确
   - 图片路径使用绝对路径
   - 添加正确的样式，特别是宽高和盒模型

3. **队列管理问题**
   - 使用key去重机制避免重复弹窗
   - 使用clearAllModals清空队列
   - 检查异步操作是否正确关闭

4. **输入框验证失败**
   - 确保validator函数返回布尔值
   - 提供明确的错误提示信息
