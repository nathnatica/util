import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Constraint;
import com.google.common.collect.Constraints;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.io.Files;

public class GuavaTest {

	String name, nickName;
	GuavaTest temp;

	public GuavaTest(String name, String nickName, GuavaTest temp) {
		this.name = name;
		this.nickName = nickName;
		this.temp = temp;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof GuavaTest) {
			GuavaTest that = (GuavaTest)object;
			return Objects.equal(this.name, that.name)
				&& Objects.equal(this.nickName, that.nickName)
				&& Objects.equal(this.temp, that.temp);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name, nickName, temp);
	}

	@Override
	public String toString() {
		Field[] fields = this.getClass().getFields();
		ToStringHelper helper = Objects.toStringHelper(this);

		for (Field f : fields) {
			try {
				helper.add(f.getName(), f.get(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return helper.toString();
	}

	public String preferredName() {
		return Objects.firstNonNull(nickName, name);
	}

	public void check() {
		Preconditions.checkState(name != "ERR", "state error %s", "message");
		Preconditions.checkNotNull(nickName);
		Preconditions.checkArgument(name != "ERR", "state error %s", "message");
	}

//	public void charMatcher() {
    public static void main(String[] args) {
        String name = "J3o-hn";
		String id = CharMatcher.DIGIT.or(CharMatcher.is('-')).negate().retainFrom(name);
        System.out.println(id); // John
        
		String id2 = CharMatcher.inRange('a', 'h').removeFrom(id);
        System.out.println(id2); // Jon
        
//        CharMatcher.JAVA_WHITESPACE
//        CharMatcher.ASCII
//               
//                CharMatcher.is('');
        
            
        
        
    }

	// predicate
	public class ShouldNotHaveDigitsInLoginPredicate implements Predicate<GuavaTest> {
		public boolean apply(GuavaTest test) {
			Preconditions.checkNotNull(test);
			return CharMatcher.DIGIT.retainFrom(test.name).length() == 0;
		}
	}

	// function
	public class FullNameFunction implements Function<GuavaTest, String> {
		public String apply(GuavaTest test) {
			Preconditions.checkNotNull(test);
			return test.name + " " + test.nickName;
		}
	}

	public void transform() {
		List<GuavaTest> l = Lists.newArrayList(new GuavaTest("","",null), new GuavaTest("","",null));
		@SuppressWarnings("unused")
		List<String> fullnames = Lists.transform(l, new FullNameFunction());
	}

	public void filter() {
		List<GuavaTest> list = Lists.newArrayList(new GuavaTest("","",null), new GuavaTest("","",null));
		@SuppressWarnings("unused")
		Collection<GuavaTest> after = Collections2.filter(list, new ShouldNotHaveDigitsInLoginPredicate());
	}

	public void caseFormat() {
		String pearName = "Really_Fucked_Up_PHP_PearConvention";
		@SuppressWarnings("unused")
		String javaAndCPPName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, pearName);
		@SuppressWarnings("unused")
		String sqlName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, pearName);
	}


	public void spliter() {
		@SuppressWarnings("unused")
		Iterable<String> pieces = Splitter.on(',').split("trivial,example");
		Splitter.on(",\\s*");

		//yields ["foo","bar","quux"]
		Splitter.on(",").trimResults().omitEmptyStrings().split(" foo,,bar, quux,");
	}

	public void file() throws Exception {
		File from = null;
		File to = null;

		Files.move(from, to);
	}

	public void collections() {
		// immutable collections
		// multimaps, multisets, bimaps

		// multimap is basically a map that can have many values for a single key
		Multimap<Integer, String> multiMap = HashMultimap.create();
			multiMap.put(1, "a");
			multiMap.put(2, "b");
			multiMap.put(3, "c");
			multiMap.put(1, "a2");

		@SuppressWarnings("unused")
		Multimap<Integer, String> multimap = ImmutableSetMultimap.of(1, "a", 2, "b", 3, "c", 4, "a2");

		@SuppressWarnings("unused")
		Multimap<Integer, String> multimap2 = new ImmutableSetMultimap.Builder<Integer, String>()
			.put(1, "a")
			.put(2, "b")
			.put(3, "c")
			.put(1, "a2")
			.build();

		//bimap is a map that have only unique values
		BiMap<Integer, String> bimap = HashBiMap.create();
		bimap.put(1, "a");
		bimap.put(2, "b");
		bimap.put(3, "c");

		// and can inverse
		@SuppressWarnings("unused")
		BiMap<String, Integer> inversedMap = bimap.inverse();



		// comparator-related utilities
		// forwarding collections, constrained collections
		// some functional programming support like (filter/transform/etc.)
		// just google google collections video
	}


	//constraints
	Constraint<GuavaTest> loginMustStartWithR = new Constraint<GuavaTest>() {
		public GuavaTest checkElement(GuavaTest user) {
			Preconditions.checkNotNull(user);

			if (user.name.startsWith("r")) {
				throw new IllegalArgumentException("GTFO, you are not Rrrrrr");
			}
			return user;
		}
	};

	public void shouldConstraintCollection() {
		Collection<GuavaTest> users = Lists.newArrayList(new GuavaTest("name","nick",null));
		Collection<GuavaTest> usersThatStartWithR = Constraints.constrainedCollection(users, loginMustStartWithR);

		usersThatStartWithR.add(new GuavaTest("","",null));

		Collection<GuavaTest> notNullUsers = Constraints.constrainedCollection(users, Constraints.notNull());
		notNullUsers.add(null);
	}


	@SuppressWarnings("unchecked")
	public void table(){
		Table<Integer, String, String> table = HashBasedTable.create();
		table.put(1, "a", "1a");
		table.put(1, "b", "1b");
		table.put(2, "a", "2a");
		table.put(2, "b", "2b");

		@SuppressWarnings("unused")
		Table transponedTable = Tables.transpose(table);
	}


	// she says jewel of common.collect.
	public void mapMaker() {
		@SuppressWarnings("unused")
		ConcurrentMap<String, GuavaTest> recommendations =
			//new MapMaker().weakKeys().expiration(10, TimeUnit.MINUTES)
			new MapMaker().weakKeys().expiration(10, TimeUnit.SECONDS)
			.makeComputingMap(
				new Function<String, GuavaTest>() {
					public GuavaTest apply(String a) {
						//return doSomething(a);
						return null;
					}
				}
		);
	}








	public static void mainx(String[] args) {
		GuavaTest a = new GuavaTest("John", null, null);
		System.out.println(a.hashCode());
		System.out.println(a.toString());
		System.out.println(a.preferredName());
	}
}

