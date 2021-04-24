package org.me.ByBlueHeart.HDebugClient.Modules.Misc;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.EventTarget;
import net.blueheart.hdebug.event.PacketEvent;
import net.blueheart.hdebug.features.module.Module;
import net.blueheart.hdebug.features.module.ModuleCategory;
import net.blueheart.hdebug.features.module.ModuleInfo;
import net.blueheart.hdebug.value.BoolValue;
import net.blueheart.hdebug.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import org.me.ByBlueHeart.HDebugClient.Modules.Combat.Aura;

import java.util.Random;

@ModuleInfo(name = "AutoL", description = "Auto Type L To Killed Player.", category = ModuleCategory.MISC)
public class AutoL extends Module {
    Aura killAura = (Aura) HDebug.moduleManager.getModule(Aura.class);
    int Killer = 0;
    private final ListValue ModeValue = new ListValue("Mode", new String[]{"ChinaHypixel", "HuaYuTing"}, "ChinaHypixel");
    private final ListValue TextValue = new ListValue("TextMode", new String[]{"Abuse", "Political", "SZJ", "Null"}, "Abuse");
    private final BoolValue ADValue = new BoolValue("AD", true);
    private final BoolValue AbuseValue = new BoolValue("Text", true);
    private final BoolValue WdrValue = new BoolValue("Wdr", true);
    private final BoolValue PrefixValue = new BoolValue("Prefix", true);
    String[] AbuseText = {"我告诉你这样的情况你还需要明白了解的不是吗", "我这埋汰你呢都是没速度和我继续的比拟呢不是奥妙",
            "我好象你爸爸似的你难道自己不清楚这样情况埃", "你这样的蜗牛就是完全没什么速度反抗你爸爸我埃",
            "我可认为你这样的垃圾好象完全没什么力量似的", "然后你完全没有力量你明白你的扣字垃圾吗。呵呵。废物"
            , "我好象你爸爸似的随便殴打你这样的事实情况了埃",
            "你为什么在这里耀武扬威"
            , "现在了解什么情况你完全垃圾似的知道了没埃"
            , "你好象垃圾似的你怎么和我对抗呢，呵呵你出来告诉我吗 然后你完全没有速度你明白什么情况吗，垃圾似的出来埃",
            "然后我随便殴打你这样的垃圾似的事实的不是吗", "你自己好象我儿子似的只能污蔑我还是怎么埃",
            "我这理解你是垃圾速度和我继续的比拟呢对吗", "我就是你父亲似的随便殴打你完全没什么力量似的",
            "父亲我随便殴打你这样的事实情况了啊弟弟", "难道不是那么出来我现在不随便殴打你吗青年", "你自己现在不了解什么还是怎么殴打飞机的爸爸",
            "我怎么感觉你和我没脾气的儿子是的反抗爸爸的", "儿子你现在自己怎么反抗的爸爸的难道什么情况啊", "小伙子啊爸爸随意的打的你了啊你明白什么情况",
            "你就是完全没什么力量反抗我还是怎么啊弟弟", "我这给你打击的哭泣的话你都是垃圾速度和我继续呢"
            , "麻烦你现在了解什么情况你出来告诉大家知道了吗", "你好象垃圾似的你有什么脾气告诉我知道了没埃",
            "你不知道爸爸我的速度可以完全吧你抹杀了啊你怎么和我相提并论了阿", "为什么在这里唧唧喳喳"
            , "儿子你现在自己怎么反抗的爸爸的难道什么情况啊",
            "我怎么国家你和我没脾气的儿子是的什么殴打"
            , "现在的你看见你爹的各种速度害怕了还是怎么的 孩子你现在的可有意思了吗你明白什么情况了吗 ",
            "你干什么啊在你登峰造极的爸爸我面前班门弄斧是不是啊?", "你是不是无可奈何了啊?小伙子是不是看见你爹爹我的言语畏惧了啊?",
            "你不知道爸爸我的速度可以完全吧你抹杀了啊你怎么和我相提并论了阿", "你现在是不是坐在电 脑前手心 出汗呢，你是不是紧张了呢",
            "你是不是八仙过海的来狗你登峰造极的爹爹我啊,用你华丽的言语攻击我啊, 小伙子.你怎么是个瓮中之鳖啊.我草你麻痹的无名小卒还大言不惭的吹嘘啊",
            "你爸爸我的速度揍你足够了", "我现在觉得你完全没有力气你自己清楚吗，呵呵垃圾似的", "然后你开始唠嗑你可以反抗还是杂的了，废物似的明白吗",
            "你好象垃圾似的你有什么脾气告诉我知道了没埃",
            "那么我们现在随便的唠嗑下你自己准备完全了吗",
            "你自己好象残酷恶霸事实你告诉我啊少年"
            , "那么我们现在就立刻唠嗑一下请你不要逃避好吗",
            "你跟本没有速度你我面前什么不能开始了啊是吗?",
            "你现在什么自己没有速度你现在和我唠嗑一下能吗?"
            , "你能够现在带着词汇立刻滚蛋好吗? 你的反抗好没有力气啊?",
            "然后你觉得你可以唠嗑还是杂的了你出来告诉我啊，儿子似的",
            "我的残酷速度随便殴打你这样的事实情况了埃", "还真是个井底之蛙，我都不想再打击你了",
            "你认为你就这么跟我说几句话旧能跟你大哥我抗衡了吗", "你这样没什么文化水平似的垃圾怎么和我开始",
            "我和你继续的开始唠嗑怎么的告诉大家情况呢蜗牛埃", "为什么在这里叽叽喳喳。你认为你那恶心的词汇可以伤到你爸爸我"
            , "你自己不清楚你完全垃圾似的没有水平还是怎么的啊. 我好象你爸爸似的不了解情况还是怎么的开始唠嗑啊. 我告诉你这个垃圾似的只能浪费时间逃避我了"
            , "儿子我好象你爸爸这样的情况你自己告诉我似的", "我这不是侮辱你恩吗也没速度和忘记下的呢", "但是你怎么反抗你爸爸我埃出来告诉我和大家"
            , "我就是随便和你开始但是你怎么反抗你爸爸我", "你认为就你这点词汇能把我打倒在这小小的网络世界中吗"
    };
    String[] PoliticalText = {"中国是世界上历史最悠久的国家之一", "中国各族人民共同创造了光辉灿烂的文化，具有光荣的革命传统", "一八四〇年以后，封建的中国逐渐变成半殖民地、半封建的国家", "中国人民为国家独立、民族解放和民主自由进行了前仆后继的英勇奋斗", "二十世纪，中国发生了翻天覆地的伟大历史变革", "一九一一年孙中山先生领导的辛亥革命，废除了封建帝制，创立了中华民国", "但是，中国人民反对帝国主义和封建主义的历史任务还没有完成", "一九四九年，以毛泽东主席为领袖的中国共产党领导中国各族人民，在经历了长期的艰难曲折的武装斗争和其他形式的斗争以后，终于推翻了帝国主义、封建主义和官僚资本主义的统治，取得了新民主主义革命的伟大胜利，建立了中华人民共和国", "从此，中国人民掌握了国家的权力，成为国家的主人", "中华人民共和国成立以后，我国社会逐步实现了由新民主主义到社会主义的过渡", "生产资料私有制的社会主义改造已经完成，人剥削人的制度已经消灭，社会主义制度已经确立", "工人阶级领导的、以工农联盟为基础的人民民主专政，实质上即无产阶级专政，得到巩固和发展", "中国人民和中国人民解放军战胜了帝国主义、霸权主义的侵略、破坏和武装挑衅，维护了国家的独立和安全，增强了国防", "经济建设取得了重大的成就，独立的、比较完整的社会主义工业体系已经基本形成，农业生产显著提高", "教育、科学、文化等事业有了很大的发展，社会主义思想教育取得了明显的成效", "广大人民的生活有了较大的改善", "中国新民主主义革命的胜利和社会主义事业的成就，是中国共产党领导中国各族人民，在马克思列宁主义、毛泽东思想的指引下，坚持真理，修正错误，战胜许多艰难险阻而取得的", "我国将长期处于社会主义初级阶段", "国家的根本任务是，沿着中国特色社会主义道路，集中力量进行社会主义现代化建设", "中国各族人民将继续在中国共产党领导下，在马克思列宁主义、毛泽东思想、邓小平理论、“三个代表”重要思想、科学发展观、习近平新时代中国特色社会主义思想指引下，坚持人民民主专政，坚持社会主义道路，坚持改革开放，不断完善社会主义的各项制度，发展社会主义市场经济，发展社会主义民主，健全社会主义法治，贯彻新发展理念，自力更生，艰苦奋斗，逐步实现工业、农业、国防和科学技术的现代化，推动物质文明、政治文明、精神文明、社会文明、生态文明协调发展，把我国建设成为富强民主文明和谐美丽的社会主义现代化强国，实现中华民族伟大复兴", "在我国，剥削阶级作为阶级已经消灭，但是阶级斗争还将在一定范围内长期存在", "中国人民对敌视和破坏我国社会主义制度的国内外的敌对势力和敌对分子，必须进行斗争", "台湾是中华人民共和国的神圣领土的一部分", "完成统一祖国的大业是包括台湾同胞在内的全中国人民的神圣职责", "社会主义的建设事业必须依靠工人、农民和知识分子，团结一切可以团结的力量", "在长期的革命、建设、改革过程中，已经结成由中国共产党领导的，有各民主党派和各人民团体参加的，包括全体社会主义劳动者、社会主义事业的建设者、拥护社会主义的爱国者、拥护祖国统一和致力于中华民族伟大复兴的爱国者的广泛的爱国统一战线，这个统一战线将继续巩固和发展", "中国人民政治协商会议是有广泛代表性的统一战线组织，过去发挥了重要的历史作用，今后在国家政治生活、社会生活和对外友好活动中，在进行社会主义现代化建设、维护国家的统一和团结的斗争中，将进一步发挥它的重要作用", "中国共产党领导的多党合作和政治协商制度将长期存在和发展", "中华人民共和国是全国各族人民共同缔造的统一的多民族国家", "平等团结互助和谐的社会主义民族关系已经确立，并将继续加强", "在维护民族团结的斗争中，要反对大民族主义，主要是大汉族主义，也要反对地方民族主义", "国家尽一切努力，促进全国各民族的共同繁荣", "中国革命、建设、改革的成就是同世界人民的支持分不开的", "中国的前途是同世界的前途紧密地联系在一起的", "中国坚持独立自主的对外政策，坚持互相尊重主权和领土完整、互不侵犯、互不干涉内政、平等互利、和平共处的五项原则，坚持和平发展道路，坚持互利共赢开放战略，发展同各国的外交关系和经济、文化交流，推动构建人类命运共同体", "坚持反对帝国主义、霸权主义、殖民主义，加强同世界各国人民的团结，支持被压迫民族和发展中国家争取和维护民族独立、发展民族经济的正义斗争，为维护世界和平和促进人类进步事业而努力", "本宪法以法律的形式确认了中国各族人民奋斗的成果，规定了国家的根本制度和根本任务，是国家的根本法，具有最高的法律效力", "全国各族人民、一切国家机关和武装力量、各政党和各社会团体、各企业事业组织，都必须以宪法为根本的活动准则，并且负有维护宪法尊严、保证宪法实施的职责", "中华人民共和国是工人阶级领导的、以工农联盟为基础的人民民主专政的社会主义国家", "社会主义制度是中华人民共和国的根本制度", "中国共产党领导是中国特色社会主义最本质的特征", "禁止任何组织或者个人破坏社会主义制度", "中华人民共和国的一切权力属于人民", "人民行使国家权力的机关是全国人民代表大会和地方各级人民代表大会", "人民依照法律规定，通过各种途径和形式，管理国家事务，管理经济和文化事业，管理社会事务", "中华人民共和国的国家机构实行民主集中制的原则", "全国人民代表大会和地方各级人民代表大会都由民主选举产生，对人民负责，受人民监督", "国家行政机关、监察机关、审判机关、检察机关都由人民代表大会产生，对它负责，受它监督", "中央和地方的国家机构职权的划分，遵循在中央的统一领导下，充分发挥地方的主动性、积极性的原则", "中华人民共和国各民族一律平等", "国家保障各少数民族的合法的权利和利益，维护和发展各民族的平等团结互助和谐关系", "禁止对任何民族的歧视和压迫，禁止破坏民族团结和制造民族分裂的行为", "国家根据各少数民族的特点和需要，帮助各少数民族地区加速经济和文化的发展", "各少数民族聚居的地方实行区域自治，设立自治机关，行使自治权", "各民族自治地方都是中华人民共和国不可分离的部分", "各民族都有使用和发展自己的语言文字的自由，都有保持或者改革自己的风俗习惯的自由", "中华人民共和国实行依法治国，建设社会主义法治国家", "国家维护社会主义法制的统一和尊严", "一切法律、行政法规和地方性法规都不得同宪法相抵触", "一切国家机关和武装力量、各政党和各社会团体、各企业事业组织都必须遵守宪法和法律", "一切违反宪法和法律的行为，必须予以追究", "任何组织或者个人都不得有超越宪法和法律的特权", "中华人民共和国的社会主义经济制度的基础是生产资料的社会主义公有制，即全民所有制和劳动群众集体所有制", "社会主义公有制消灭人剥削人的制度，实行各尽所能、按劳分配的原则", "国家在社会主义初级阶段，坚持公有制为主体、多种所有制经济共同发展的基本经济制度，坚持按劳分配为主体、多种分配方式并存的分配制度", "国有经济，即社会主义全民所有制经济，是国民经济中的主导力量", "国家保障国有经济的巩固和发展", "农村集体经济组织实行家庭承包经营为基础、统分结合的双层经营体制", "农村中的生产、供销、信用、消费等各种形式的合作经济，是社会主义劳动群众集体所有制经济", "参加农村集体经济组织的劳动者，有权在法律规定的范围内经营自留地、自留山、家庭副业和饲养自留畜", "城镇中的手工业、工业、建筑业、运输业、商业、服务业等行业的各种形式的合作经济，都是社会主义劳动群众集体所有制经济", "国家保护城乡集体经济组织的合法的权利和利益，鼓励、指导和帮助集体经济的发展", "矿藏、水流、森林、山岭、草原、荒地、滩涂等自然资源，都属于国家所有，即全民所有", "由法律规定属于集体所有的森林和山岭、草原、荒地、滩涂除外", "国家保障自然资源的合理利用，保护珍贵的动物和植物", "禁止任何组织或者个人用任何手段侵占或者破坏自然资源", "城市的土地属于国家所有", "农村和城市郊区的土地，除由法律规定属于国家所有的以外，属于集体所有", "宅基地和自留地、自留山，也属于集体所有", "国家为了公共利益的需要，可以依照法律规定对土地实行征收或者征用并给予补偿", "任何组织或者个人不得侵占、买卖或者以其他形式非法转让土地", "土地的使用权可以依照法律的规定转让", "一切使用土地的组织和个人必须合理地利用土地", "在法律规定范围内的个体经济、私营经济等非公有制经济，是社会主义市场经济的重要组成部分", "国家保护个体经济、私营经济等非公有制经济的合法的权利和利益", "国家鼓励、支持和引导非公有制经济的发展，并对非公有制经济依法实行监督和管理", "社会主义的公共财产神圣不可侵犯", "国家保护社会主义的公共财产", "禁止任何组织或者个人用任何手段侵占或者破坏国家的和集体的财产", "公民的合法的私有财产不受侵犯", "国家依照法律规定保护公民的私有财产权和继承权", "国家为了公共利益的需要，可以依照法律规定对公民的私有财产实行征收或者征用并给予补偿", "国家通过提高劳动者的积极性和技术水平，推广先进的科学技术，完善经济管理体制和企业经营管理制度，实行各种形式的社会主义责任制，改进劳动组织，以不断提高劳动生产率和经济效益，发展社会生产力", "国家厉行节约，反对浪费", "国家合理安排积累和消费，兼顾国家、集体和个人的利益，在发展生产的基础上，逐步改善人民的物质生活和文化生活", "国家建立健全同经济发展水平相适应的社会保障制度", "国家实行社会主义市场经济", "国家加强经济立法，完善宏观调控", "国家依法禁止任何组织或者个人扰乱社会经济秩序", "国有企业在法律规定的范围内有权自主经营", "国有企业依照法律规定，通过职工代表大会和其他形式，实行民主管理", "集体经济组织在遵守有关法律的前提下，有独立进行经济活动的自主权", "集体经济组织实行民主管理，依照法律规定选举和罢免管理人员，决定经营管理的重大问题", "中华人民共和国允许外国的企业和其他经济组织或者个人依照中华人民共和国法律的规定在中国投资，同中国的企业或者其他经济组织进行各种形式的经济合作", "在中国境内的外国企业和其他外国经济组织以及中外合资经营的企业，都必须遵守中华人民共和国的法律", "它们的合法的权利和利益受中华人民共和国法律的保护", "国家发展社会主义的教育事业，提高全国人民的科学文化水平", "国家举办各种学校，普及初等义务教育，发展中等教育、职业教育和高等教育，并且发展学前教育", "国家发展各种教育设施，扫除文盲，对工人、农民、国家工作人员和其他劳动者进行政治、文化、科学、技术、业务的教育，鼓励自学成才", "国家鼓励集体经济组织、国家企业事业组织和其他社会力量依照法律规定举办各种教育事业", "国家推广全国通用的普通话", "国家发展自然科学和社会科学事业，普及科学和技术知识，奖励科学研究成果和技术发明创造", "国家发展医疗卫生事业，发展现代医药和我国传统医药，鼓励和支持农村集体经济组织、国家企业事业组织和街道组织举办各种医疗卫生设施，开展群众性的卫生活动，保护人民健康", "国家发展体育事业，开展群众性的体育活动，增强人民体质", "国家发展为人民服务、为社会主义服务的文学艺术事业、新闻广播电视事业、出版发行事业、图书馆博物馆文化馆和其他文化事业，开展群众性的文化活动", "国家保护名胜古迹、珍贵文物和其他重要历史文化遗产", "国家培养为社会主义服务的各种专业人才，扩大知识分子的队伍，创造条件，充分发挥他们在社会主义现代化建设中的作用", "国家通过普及理想教育、道德教育、文化教育、纪律和法制教育，通过在城乡不同范围的群众中制定和执行各种守则、公约，加强社会主义精神文明的建设", "国家倡导社会主义核心价值观，提倡爱祖国、爱人民、爱劳动、爱科学、爱社会主义的公德，在人民中进行爱国主义、集体主义和国际主义、共产主义的教育，进行辩证唯物主义和历史唯物主义的教育，反对资本主义的、封建主义的和其他的腐朽思想", "国家推行计划生育，使人口的增长同经济和社会发展计划相适应", "国家保护和改善生活环境和生态环境，防治污染和其他公害", "国家组织和鼓励植树造林，保护林木", "一切国家机关实行精简的原则，实行工作责任制，实行工作人员的培训和考核制度，不断提高工作质量和工作效率，反对官僚主义", "一切国家机关和国家工作人员必须依靠人民的支持，经常保持同人民的密切联系，倾听人民的意见和建议，接受人民的监督，努力为人民服务", "国家工作人员就职时应当依照法律规定公开进行宪法宣誓", "国家维护社会秩序，镇压叛国和其他危害国家安全的犯罪活动，制裁危害社会治安、破坏社会主义经济和其他犯罪的活动，惩办和改造犯罪分子", "中华人民共和国的武装力量属于人民", "它的任务是巩固国防，抵抗侵略，保卫祖国，保卫人民的和平劳动，参加国家建设事业，努力为人民服务", "国家加强武装力量的革命化、现代化、正规化的建设，增强国防力量", "中华人民共和国的行政区域划分如下", "（一）全国分为省、自治区、直辖市", "（二）省、自治区分为自治州、县、自治县、市", "（三）县、自治县分为乡、民族乡、镇", "直辖市和较大的市分为区、县", "自治州分为县、自治县、市", "自治区、自治州、自治县都是民族自治地方", "国家在必要时得设立特别行政区", "在特别行政区内实行的制度按照具体情况由全国人民代表大会以法律规定", "中华人民共和国保护在中国境内的外国人的合法权利和利益，在中国境内的外国人必须遵守中华人民共和国的法律", "中华人民共和国对于因为政治原因要求避难的外国人，可以给予受庇护的权利", "凡具有中华人民共和国国籍的人都是中华人民共和国公民", "中华人民共和国公民在法律面前一律平等", "国家尊重和保障人权", "任何公民享有宪法和法律规定的权利，同时必须履行宪法和法律规定的义务", "中华人民共和国年满十八周岁的公民，不分民族、种族、性别、职业、家庭出身、宗教信仰、教育程度、财产状况、居住期限，都有选举权和被选举权", "但是依照法律被剥夺政治权利的人除外", "中华人民共和国公民有言论、出版、集会、结社、游行、示威的自由", "中华人民共和国公民有宗教信仰自由", "任何国家机关、社会团体和个人不得强制公民信仰宗教或者不信仰宗教，不得歧视信仰宗教的公民和不信仰宗教的公民", "国家保护正常的宗教活动", "任何人不得利用宗教进行破坏社会秩序、损害公民身体健康、妨碍国家教育制度的活动", "宗教团体和宗教事务不受外国势力的支配", "中华人民共和国公民的人身自由不受侵犯", "任何公民，非经人民检察院批准或者决定或者人民法院决定，并由公安机关执行，不受逮捕", "禁止非法拘禁和以其他方法非法剥夺或者限制公民的人身自由，禁止非法搜查公民的身体", "中华人民共和国公民的人格尊严不受侵犯", "禁止用任何方法对公民进行侮辱、诽谤和诬告陷害", "中华人民共和国公民的住宅不受侵犯", "禁止非法搜查或者非法侵入公民的住宅", "中华人民共和国公民的通信自由和通信秘密受法律的保护", "除因国家安全或者追查刑事犯罪的需要，由公安机关或者检察机关依照法律规定的程序对通信进行检查外，任何组织或者个人不得以任何理由侵犯公民的通信自由和通信秘密", "中华人民共和国公民对于任何国家机关和国家工作人员，有提出批评和建议的权利", "对于任何国家机关和国家工作人员的违法失职行为，有向有关国家机关提出申诉、控告或者检举的权利，但是不得捏造或者歪曲事实进行诬告陷害", "对于公民的申诉、控告或者检举，有关国家机关必须查清事实，负责处理", "任何人不得压制和打击报复", "由于国家机关和国家工作人员侵犯公民权利而受到损失的人，有依照法律规定取得赔偿的权利", "中华人民共和国公民有劳动的权利和义务", "国家通过各种途径，创造劳动就业条件，加强劳动保护，改善劳动条件，并在发展生产的基础上，提高劳动报酬和福利待遇", "劳动是一切有劳动能力的公民的光荣职责", "国有企业和城乡集体经济组织的劳动者都应当以国家主人翁的态度对待自己的劳动", "国家提倡社会主义劳动竞赛，奖励劳动模范和先进工作者", "国家提倡公民从事义务劳动", "国家对就业前的公民进行必要的劳动就业训练", "中华人民共和国劳动者有休息的权利", "国家发展劳动者休息和休养的设施，规定职工的工作时间和休假制度", "国家依照法律规定实行企业事业组织的职工和国家机关工作人员的退休制度", "退休人员的生活受到国家和社会的保障", "中华人民共和国公民在年老、疾病或者丧失劳动能力的情况下，有从国家和社会获得物质帮助的权利", "国家发展为公民享受这些权利所需要的社会保险、社会救济和医疗卫生事业", "国家和社会保障残废军人的生活，抚恤烈士家属，优待军人家属", "国家和社会帮助安排盲、聋、哑和其他有残疾的公民的劳动、生活和教育", "中华人民共和国公民有受教育的权利和义务", "国家培养青年、少年、儿童在品德、智力、体质等方面全面发展", "中华人民共和国公民有进行科学研究、文学艺术创作和其他文化活动的自由", "国家对于从事教育、科学、技术、文学、艺术和其他文化事业的公民的有益于人民的创造性工作，给以鼓励和帮助", "中华人民共和国妇女在政治的、经济的、文化的、社会的和家庭的生活等各方面享有同男子平等的权利", "国家保护妇女的权利和利益，实行男女同工同酬，培养和选拔妇女干部", "婚姻、家庭、母亲和儿童受国家的保护", "夫妻双方有实行计划生育的义务", "父母有抚养教育未成年子女的义务，成年子女有赡养扶助父母的义务", "禁止破坏婚姻自由，禁止虐待老人、妇女和儿童", "中华人民共和国保护华侨的正当的权利和利益，保护归侨和侨眷的合法的权利和利益", "中华人民共和国公民在行使自由和权利的时候，不得损害国家的、社会的、集体的利益和其他公民的合法的自由和权利", "中华人民共和国公民有维护国家统一和全国各民族团结的义务", "中华人民共和国公民必须遵守宪法和法律，保守国家秘密，爱护公共财产，遵守劳动纪律，遵守公共秩序，尊重社会公德", "中华人民共和国公民有维护祖国的安全、荣誉和利益的义务，不得有危害祖国的安全、荣誉和利益的行为", "保卫祖国、抵抗侵略是中华人民共和国每一个公民的神圣职责", "依照法律服兵役和参加民兵组织是中华人民共和国公民的光荣义务", "中华人民共和国公民有依照法律纳税的义务", "中华人民共和国全国人民代表大会是最高国家权力机关", "它的常设机关是全国人民代表大会常务委员会", "全国人民代表大会和全国人民代表大会常务委员会行使国家立法权", "全国人民代表大会由省、自治区、直辖市、特别行政区和军队选出的代表组成", "各少数民族都应当有适当名额的代表", "全国人民代表大会代表的选举由全国人民代表大会常务委员会主持", "全国人民代表大会代表名额和代表产生办法由法律规定", "全国人民代表大会每届任期五年", "全国人民代表大会任期届满的两个月以前，全国人民代表大会常务委员会必须完成下届全国人民代表大会代表的选举", "如果遇到不能进行选举的非常情况，由全国人民代表大会常务委员会以全体组成人员的三分之二以上的多数通过，可以推迟选举，延长本届全国人民代表大会的任期", "在非常情况结束后一年内，必须完成下届全国人民代表大会代表的选举", "全国人民代表大会会议每年举行一次，由全国人民代表大会常务委员会召集", "如果全国人民代表大会常务委员会认为必要，或者有五分之一以上的全国人民代表大会代表提议，可以临时召集全国人民代表大会会议", "全国人民代表大会举行会议的时候，选举主席团主持会议", "全国人民代表大会行使下列职权", "（一）修改宪法", "（二）监督宪法的实施", "（三）制定和修改刑事、民事、国家机构的和其他的基本法律", "（四）选举中华人民共和国主席、副主席", "（五）根据中华人民共和国主席的提名，决定国务院总理的人选", "根据国务院总理的提名，决定国务院副总理、国务委员、各部部长、各委员会主任、审计长、秘书长的人选", "（六）选举中央军事委员会主席", "根据中央军事委员会主席的提名，决定中央军事委员会其他组成人员的人选", "（七）选举国家监察委员会主任", "（八）选举最高人民法院院长", "（九）选举最高人民检察院检察长", "（十）审查和批准国民经济和社会发展计划和计划执行情况的报告", "（十一）审查和批准国家的预算和预算执行情况的报告", "（十二）改变或者撤销全国人民代表大会常务委员会不适当的决定", "（十三）批准省、自治区和直辖市的建置", "（十四）决定特别行政区的设立及其制度", "（十五）决定战争和和平的问题", "（十六）应当由最高国家权力机关行使的其他职权", "全国人民代表大会有权罢免下列人员", "（一）中华人民共和国主席、副主席", "（二）国务院总理、副总理、国务委员、各部部长、各委员会主任、审计长、秘书长", "（三）中央军事委员会主席和中央军事委员会其他组成人员", "（四）国家监察委员会主任", "（五）最高人民法院院长", "（六）最高人民检察院检察长", "宪法的修改，由全国人民代表大会常务委员会或者五分之一以上的全国人民代表大会代表提议，并由全国人民代表大会以全体代表的三分之二以上的多数通过", "法律和其他议案由全国人民代表大会以全体代表的过半数通过", "全国人民代表大会常务委员会由下列人员组成", "委员长，副委员长若干人，秘书长，委员若干人", "全国人民代表大会常务委员会组成人员中，应当有适当名额的少数民族代表", "全国人民代表大会选举并有权罢免全国人民代表大会常务委员会的组成人员", "全国人民代表大会常务委员会的组成人员不得担任国家行政机关、监察机关、审判机关和检察机关的职务", "全国人民代表大会常务委员会每届任期同全国人民代表大会每届任期相同，它行使职权到下届全国人民代表大会选出新的常务委员会为止", "委员长、副委员长连续任职不得超过两届", "全国人民代表大会常务委员会行使下列职权", "（一）解释宪法，监督宪法的实施", "（二）制定和修改除应当由全国人民代表大会制定的法律以外的其他法律", "（三）在全国人民代表大会闭会期间，对全国人民代表大会制定的法律进行部分补充和修改，但是不得同该法律的基本原则相抵触", "（四）解释法律", "（五）在全国人民代表大会闭会期间，审查和批准国民经济和社会发展计划、国家预算在执行过程中所必须作的部分调整方案", "（六）监督国务院、中央军事委员会、国家监察委员会、最高人民法院和最高人民检察院的工作", "（七）撤销国务院制定的同宪法、法律相抵触的行政法规、决定和命令", "（八）撤销省、自治区、直辖市国家权力机关制定的同宪法、法律和行政法规相抵触的地方性法规和决议", "（九）在全国人民代表大会闭会期间，根据国务院总理的提名，决定部长、委员会主任、审计长、秘书长的人选", "（十）在全国人民代表大会闭会期间，根据中央军事委员会主席的提名，决定中央军事委员会其他组成人员的人选", "（十一）根据国家监察委员会主任的提请，任免国家监察委员会副主任、委员", "（十二）根据最高人民法院院长的提请，任免最高人民法院副院长、审判员、审判委员会委员和军事法院院长", "（十三）根据最高人民检察院检察长的提请，任免最高人民检察院副检察长、检察员、检察委员会委员和军事检察院检察长，并且批准省、自治区、直辖市的人民检察院检察长的任免", "（十四）决定驻外全权代表的任免", "（十五）决定同外国缔结的条约和重要协定的批准和废除", "（十六）规定军人和外交人员的衔级制度和其他专门衔级制度", "（十七）规定和决定授予国家的勋章和荣誉称号", "（十八）决定特赦", "（十九）在全国人民代表大会闭会期间，如果遇到国家遭受武装侵犯或者必须履行国际间共同防止侵略的条约的情况，决定战争状态的宣布", "（二十）决定全国总动员或者局部动员", "（二十一）决定全国或者个别省、自治区、直辖市进入紧急状态", "（二十二）全国人民代表大会授予的其他职权", "全国人民代表大会常务委员会委员长主持全国人民代表大会常务委员会的工作，召集全国人民代表大会常务委员会会议", "副委员长、秘书长协助委员长工作", "委员长、副委员长、秘书长组成委员长会议，处理全国人民代表大会常务委员会的重要日常工作", "全国人民代表大会常务委员会对全国人民代表大会负责并报告工作", "全国人民代表大会设立民族委员会、宪法和法律委员会、财政经济委员会、教育科学文化卫生委员会、外事委员会、华侨委员会和其他需要设立的专门委员会", "在全国人民代表大会闭会期间，各专门委员会受全国人民代表大会常务委员会的领导", "各专门委员会在全国人民代表大会和全国人民代表大会常务委员会领导下，研究、审议和拟订有关议案", "全国人民代表大会和全国人民代表大会常务委员会认为必要的时候，可以组织关于特定问题的调查委员会，并且根据调查委员会的报告，作出相应的决议", "调查委员会进行调查的时候，一切有关的国家机关、社会团体和公民都有义务向它提供必要的材料", "全国人民代表大会代表和全国人民代表大会常务委员会组成人员，有权依照法律规定的程序分别提出属于全国人民代表大会和全国人民代表大会常务委员会职权范围内的议案", "全国人民代表大会代表在全国人民代表大会开会期间，全国人民代表大会常务委员会组成人员在常务委员会开会期间，有权依照法律规定的程序提出对国务院或者国务院各部、各委员会的质询案", "受质询的机关必须负责答复", "全国人民代表大会代表，非经全国人民代表大会会议主席团许可，在全国人民代表大会闭会期间非经全国人民代表大会常务委员会许可，不受逮捕或者刑事审判", "全国人民代表大会代表在全国人民代表大会各种会议上的发言和表决，不受法律追究", "全国人民代表大会代表必须模范地遵守宪法和法律，保守国家秘密，并且在自己参加的生产、工作和社会活动中，协助宪法和法律的实施", "全国人民代表大会代表应当同原选举单位和人民保持密切的联系，听取和反映人民的意见和要求，努力为人民服务", "全国人民代表大会代表受原选举单位的监督", "原选举单位有权依照法律规定的程序罢免本单位选出的代表", "全国人民代表大会和全国人民代表大会常务委员会的组织和工作程序由法律规定", "中华人民共和国主席、副主席由全国人民代表大会选举", "有选举权和被选举权的年满四十五周岁的中华人民共和国公民可以被选为中华人民共和国主席、副主席", "中华人民共和国主席、副主席每届任期同全国人民代表大会每届任期相同", "中华人民共和国主席根据全国人民代表大会的决定和全国人民代表大会常务委员会的决定，公布法律，任免国务院总理、副总理、国务委员、各部部长、各委员会主任、审计长、秘书长，授予国家的勋章和荣誉称号，发布特赦令，宣布进入紧急状态，宣布战争状态，发布动员令", "中华人民共和国主席代表中华人民共和国，进行国事活动，接受外国使节", "根据全国人民代表大会常务委员会的决定，派遣和召回驻外全权代表，批准和废除同外国缔结的条约和重要协定", "中华人民共和国副主席协助主席工作", "中华人民共和国副主席受主席的委托，可以代行主席的部分职权", "中华人民共和国主席缺位的时候，由副主席继任主席的职位", "中华人民共和国副主席缺位的时候，由全国人民代表大会补选", "中华人民共和国主席、副主席都缺位的时候，由全国人民代表大会补选", "在补选以前，由全国人民代表大会常务委员会委员长暂时代理主席职位", "中华人民共和国国务院，即中央人民政府，是最高国家权力机关的执行机关，是最高国家行政机关", "国务院由下列人员组成", "总理，副总理若干人，国务委员若干人，各部部长，各委员会主任，审计长，秘书长", "国务院实行总理负责制", "各部、各委员会实行部长、主任负责制", "国务院的组织由法律规定", "国务院每届任期同全国人民代表大会每届任期相同", "总理、副总理、国务委员连续任职不得超过两届", "总理领导国务院的工作", "副总理、国务委员协助总理工作", "总理、副总理、国务委员、秘书长组成国务院常务会议", "总理召集和主持国务院常务会议和国务院全体会议", "国务院行使下列职权", "（一）根据宪法和法律，规定行政措施，制定行政法规，发布决定和命令", "（二）向全国人民代表大会或者全国人民代表大会常务委员会提出议案", "（三）规定各部和各委员会的任务和职责，统一领导各部和各委员会的工作，并且领导不属于各部和各委员会的全国性的行政工作", "（四）统一领导全国地方各级国家行政机关的工作，规定中央和省、自治区、直辖市的国家行政机关的职权的具体划分", "（五）编制和执行国民经济和社会发展计划和国家预算", "（六）领导和管理经济工作和城乡建设、生态文明建设", "（七）领导和管理教育、科学、文化、卫生、体育和计划生育工作", "（八）领导和管理民政、公安、司法行政等工作", "（九）管理对外事务，同外国缔结条约和协定", "（十）领导和管理国防建设事业", "（十一）领导和管理民族事务，保障少数民族的平等权利和民族自治地方的自治权利", "（十二）保护华侨的正当的权利和利益，保护归侨和侨眷的合法的权利和利益", "（十三）改变或者撤销各部、各委员会发布的不适当的命令、指示和规章", "（十四）改变或者撤销地方各级国家行政机关的不适当的决定和命令", "（十五）批准省、自治区、直辖市的区域划分，批准自治州、县、自治县、市的建置和区域划分", "（十六）依照法律规定决定省、自治区、直辖市的范围内部分地区进入紧急状态", "（十七）审定行政机构的编制，依照法律规定任免、培训、考核和奖惩行政人员", "（十八）全国人民代表大会和全国人民代表大会常务委员会授予的其他职权", "国务院各部部长、各委员会主任负责本部门的工作", "召集和主持部务会议或者委员会会议、委务会议，讨论决定本部门工作的重大问题", "各部、各委员会根据法律和国务院的行政法规、决定、命令，在本部门的权限内，发布命令、指示和规章", "国务院设立审计机关，对国务院各部门和地方各级政府的财政收支，对国家的财政金融机构和企业事业组织的财务收支，进行审计监督", "审计机关在国务院总理领导下，依照法律规定独立行使审计监督权，不受其他行政机关、社会团体和个人的干涉", "国务院对全国人民代表大会负责并报告工作", "在全国人民代表大会闭会期间，对全国人民代表大会常务委员会负责并报告工作", "中华人民共和国中央军事委员会领导全国武装力量", "中央军事委员会由下列人员组成", "主席，副主席若干人，委员若干人", "中央军事委员会实行主席负责制", "中央军事委员会每届任期同全国人民代表大会每届任期相同", "中央军事委员会主席对全国人民代表大会和全国人民代表大会常务委员会负责", "省、直辖市、县、市、市辖区、乡、民族乡、镇设立人民代表大会和人民政府", "地方各级人民代表大会和地方各级人民政府的组织由法律规定", "自治区、自治州、自治县设立自治机关", "自治机关的组织和工作根据宪法第三章第五节、第六节规定的基本原则由法律规定", "地方各级人民代表大会是地方国家权力机关", "县级以上的地方各级人民代表大会设立常务委员会", "省、直辖市、设区的市的人民代表大会代表由下一级的人民代表大会选举", "县、不设区的市、市辖区、乡、民族乡、镇的人民代表大会代表由选民直接选举", "地方各级人民代表大会代表名额和代表产生办法由法律规定", "地方各级人民代表大会每届任期五年", "地方各级人民代表大会在本行政区域内，保证宪法、法律、行政法规的遵守和执行", "依照法律规定的权限，通过和发布决议，审查和决定地方的经济建设、文化建设和公共事业建设的计划", "县级以上的地方各级人民代表大会审查和批准本行政区域内的国民经济和社会发展计划、预算以及它们的执行情况的报告", "有权改变或者撤销本级人民代表大会常务委员会不适当的决定", "民族乡的人民代表大会可以依照法律规定的权限采取适合民族特点的具体措施", "省、直辖市的人民代表大会和它们的常务委员会，在不同宪法、法律、行政法规相抵触的前提下，可以制定地方性法规，报全国人民代表大会常务委员会备案", "设区的市的人民代表大会和它们的常务委员会，在不同宪法、法律、行政法规和本省、自治区的地方性法规相抵触的前提下，可以依照法律规定制定地方性法规，报本省、自治区人民代表大会常务委员会批准后施行"};
    String[] SZJText = {"人之初 性本善 性相近 习相远", "苟不教 性乃迁 教之道 贵以专", "昔孟母 择邻处 子不学 断机杼", "窦燕山 有义方 教五子 名俱扬", "养不教 父之过 教不严 师之惰", "子不学 非所宜 幼不学 老何为", "玉不琢 不成器 人不学 不知义", "为人子 方少时 亲师友 习礼仪", "香九龄 能温席 孝于亲 所当执", "融四岁 能让梨 弟于长 宜先知", "首孝悌 次见闻 知某数 识某文", "一而十 十而百 百而千 千而万", "三才者 天地人 三光者 日月星", "三纲者 君臣义 父子亲 夫妇顺", "曰春夏 曰秋冬 此四时 运不穷", "曰南北 曰西东 此四方 应乎中", "曰水火 木金土 此五行 本乎数", "十干者 甲至癸 十二支 子至亥", "曰黄道 日所躔 曰赤道 当中权", "赤道下 温暖极 我中华 在东北", "曰江河 曰淮济 此四渎 水之纪", "曰岱华 嵩恒衡 此五岳 山之名", "曰士农 曰工商 此四民 国之良", "曰仁义 礼智信 此五常 不容紊", "地所生 有草木 此植物 遍水陆", "有虫鱼 有鸟兽 此动物 能飞走", "稻粱菽 麦黍稷 此六谷 人所食", "马牛羊 鸡犬豕 此六畜 人所饲", "曰喜怒 曰哀惧 爱恶欲 七情具", "青赤黄 及黑白 此五色 目所识", "酸苦甘 及辛咸 此五味 口所含", "膻焦香 及腥朽 此五臭 鼻所嗅", "匏土革 木石金 丝与竹 乃八音", "曰平上 曰去入 此四声 宜调协", "高曾祖 父而身 身而子 子而孙", "自子孙 至玄曾 乃九族 人之伦", "父子恩 夫妇从 兄则友 弟则恭", "长幼序 友与朋 君则敬 臣则忠", "此十义 人所同 当师叙 勿违背", "斩齐衰 大小功 至缌麻 五服终", "礼乐射 御书数 古六艺 今不具", "惟书学 人共遵 既识字 讲说文", "有古文 大小篆 隶草继 不可乱", "若广学 惧其繁 但略说 能知原", "凡训蒙 须讲究 详训诂 明句读", "为学者 必有初 小学终 至四书", "论语者 二十篇 群弟子 记善言", "孟子者 七篇止 讲道德 说仁义", "作中庸 子思笔 中不偏 庸不易", "作大学 乃曾子 自修齐 至平治", "孝经通 四书熟 如六经 始可读", "诗书易 礼春秋 号六经 当讲求", "有连山 有归藏 有周易 三易详", "有典谟 有训诰 有誓命 书之奥", "我周公 作周礼 著六官 存治体", "大小戴 注礼记 述圣言 礼乐备", "曰国风 曰雅颂 号四诗 当讽咏", "诗既亡 春秋作 寓褒贬 别善恶", "三传者 有公羊 有左氏 有谷梁", "经既明 方读子 撮其要 记其事", "五子者 有荀扬 文中子 及老庄", "经子通 读诸史 考世系 知始终", "自羲农 至黄帝 号三皇 居上世", "唐有虞 号二帝 相揖逊 称盛世", "夏有禹 商有汤 周武王 称三王", "夏传子 家天下 四百载 迁夏社", "汤伐夏 国号商 六百载 至纣亡", "周武王 始诛纣 八百载 最长久", "周辙东 王纲坠 逞干戈 尚游说", "始春秋 终战国 五霸强 七雄出", "嬴秦氏 始兼并 传二世 楚汉争", "高祖兴 汉业建 至孝平 王莽篡", "光武兴 为东汉 四百年 终于献", "魏蜀吴 争汉鼎 号三国 迄两晋", "宋齐继 梁陈承 为南朝 都金陵", "北元魏 分东西 宇文周 与高齐", "迨至隋 一土宇 不再传 失统绪", "唐高祖 起义师 除隋乱 创国基", "二十传 三百载 梁灭之 国乃改", "梁唐晋 及汉周 称五代 皆有由", "炎宋兴 受周禅 十八传 南北混", "辽与金 皆称帝 元灭金 绝宋世", "舆图广 超前代 九十年 国祚废", "太祖兴 国大明 号洪武 都金陵", "迨成祖 迁燕京 十六世 至崇祯", "权阉肆 寇如林 李闯出 神器焚", "清世祖 膺景命 靖四方 克大定", "由康雍 历乾嘉 民安富 治绩夸", "道咸间 变乱起 始英法 扰都鄙", "同光后 宣统弱 传九帝 满清殁", "革命兴 废帝制 立宪法 建民国", "古今史 全在兹 载治乱 知兴衰", "史虽繁 读有次 史记一 汉书二", "后汉三 国志四 兼证经 参通鉴", "读史者 考实录 通古今 若亲目", "昔仲尼 师项橐 古圣贤 尚勤学", "赵中令 读鲁论 彼既仕 学且勤", "披蒲编 削竹简 彼无书 且知勉", "头悬梁 锥刺股 彼不教 自勤苦", "如囊萤 如映雪 家虽贫 学不辍", "如负薪 如挂角 身虽劳 犹苦卓", "苏老泉 二十七 始发愤 读书籍", "彼既老 犹悔迟 尔小生 宜早思", "若梁灏八十二 对大廷 魁多士", "彼既成 众称异 尔小生 宜立志", "莹八岁 能咏诗 泌七岁 能赋棋", "彼颖悟 人称奇 尔幼学 当效之", "蔡文姬 能辩琴 谢道韫 能咏吟", "彼女子 且聪敏 尔男子 当自警", "唐刘晏 方七岁 举神童 作正字", "口而诵 心而惟 朝于斯 夕于斯", "晏虽幼 身已仕 有为者 亦若是", "犬守夜 鸡司晨 苟不学 曷为人", "蚕吐丝 蜂酿蜜 人不学 不如物", "幼而学 壮而行 上致君 下泽民", "扬名声 显父母 光于前 裕于后", "人遗子 金满赢 我教子 唯一经", "勤有功 戏无益 戒之哉 宜勉力"};
    String[] NullText = {"何必怕鬼 残害你的永远是人 何必怕死 折磨你的永远是生", "仅一夜之隔，我心竟判若两人。若能避开猛烈的欢喜，自然也不会有悲痛的来袭", "白天是个开心的傻子 晚上是被情绪控制的疯子", "世俗本无罪 可无奈人心险恶 让人作呕"};

    @EventTarget
    public void onPacket(final PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            Random r = new Random();
            String Abuse = AbuseText[r.nextInt(57)];
            String Political = PoliticalText[r.nextInt(363)];
            String SZJ = SZJText[r.nextInt(118)];
            String Null = NullText[r.nextInt(4)];
            String AD = " HDebugQQ群:1128533970";
            String PrefixText = "[HDebug]";
            Entity entity = killAura.getTarget();
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            switch (ModeValue.get().toLowerCase()) {
                case "chinahypixel":
                    if (message.contains("被" + mc.thePlayer.getGameProfile().getName() + "击杀。")) {
                        String username = message.replaceAll("被" + mc.thePlayer.getGameProfile().getName() + "击杀。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains("被" + mc.thePlayer.getGameProfile().getName() + "扔下了虚空。")) {
                        String username = message.replaceAll("被" + mc.thePlayer.getGameProfile().getName() + "扔下了虚空。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 被击杀，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 被击杀，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains("被" + mc.thePlayer.getGameProfile().getName() + "杀死。")) {
                        String username = message.replaceAll("被" + mc.thePlayer.getGameProfile().getName() + "杀死。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 被塞进了戴维 · 琼斯的箱子，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 被塞进了戴维 · 琼斯的箱子，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 中箭无数，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 中箭无数，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 被逼入末路，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 被逼入末路，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 被炮弹炸死，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 被炮弹炸死，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 被扔下虚空，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 被扔下虚空，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains(" 被烤成了肉串，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。")) {
                        String username = message.replaceAll(" 被烤成了肉串，击杀者： " + mc.thePlayer.getGameProfile().getName() + "。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    if (message.contains("被" + mc.thePlayer.getGameProfile().getName() + "扔下了悬崖。")) {
                        String username = message.replaceAll("被" + mc.thePlayer.getGameProfile().getName() + "扔下了悬崖。", "");
                        if (username.contains("最终击杀！")) {
                            username = username.replaceAll("最终击杀！", "");
                        }
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("/ac " + (PrefixValue.get() ? PrefixText : "") + username + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? " " + AD : ""));
                                break;
                        }
                        mc.thePlayer.sendChatMessage((WdrValue.get() ? "/wdr " + username + " ka speed reach fly velocity ac" : ""));
                        Killer = Killer + 1;
                    }
                    break;
                case "huayuting":
                    String Name = entity.getName();
                    if (((S02PacketChat) packet).getChatComponent().getUnformattedText().indexOf("起床战争>> " + mc.thePlayer.getName()) != -1 && ((S02PacketChat) packet).getChatComponent().getUnformattedText().indexOf("杀死了") != -1 || ((S02PacketChat) packet).getChatComponent().getUnformattedText().indexOf("起床战争 >> " + mc.thePlayer.getName()) != -1 && ((S02PacketChat) packet).getChatComponent().getUnformattedText().indexOf("杀死了") != -1) {
                        switch (TextValue.get().toLowerCase()) {
                            case "abuse":
                                mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + Name + " L " + (AbuseValue.get() ? Abuse : "") + (ADValue.get() ? AD : ""));
                                break;
                            case "political":
                                mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + Name + " L " + (AbuseValue.get() ? Political : "") + (ADValue.get() ? AD : ""));
                                break;
                            case "szj":
                                mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + Name + " L " + (AbuseValue.get() ? SZJ : "") + (ADValue.get() ? AD : ""));
                                break;
                            case "null":
                                mc.thePlayer.sendChatMessage("@" + (PrefixValue.get() ? PrefixText : "") + Name + " L " + (AbuseValue.get() ? Null : "") + (ADValue.get() ? AD : ""));
                                break;
                        }
                        Killer = Killer + 1;
                    }
                    break;
            }
        }
    }

    @Override
    public String getTag() {
        return ModeValue.get() + "-" + TextValue.get() + " Kill:" + Killer;
    }
}